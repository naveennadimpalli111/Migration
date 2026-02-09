
package com.its255.viewer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Controller
@RequestMapping("/viewer3")
public class FastViewerController {
    private final ViewerConfig cfg;

    private volatile double progress = 0.0; // 0..1 during prefix indexing
    private Path filePath;
    private Object store; // ChunkedMMapRecordStore
    private PrefixIndex pidx;
    private FastRecordFilter filter;
    private RecordNavigator nav;
    private java.util.List<Integer> lastFiltered = java.util.List.of();

    public FastViewerController(ViewerConfig cfg) { this.cfg = cfg; }
    private Charset cs() { return Charset.forName(cfg.getCharset()); }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("hasFile", store != null);
        model.addAttribute("count", nav != null ? nav.size() : 0);
        model.addAttribute("position", nav != null ? nav.position() : 0);
        model.addAttribute("hasPrev", nav != null && nav.hasPrev());
        model.addAttribute("hasNext", nav != null && nav.hasNext());
        model.addAttribute("html", "");
        model.addAttribute("progress", progress);
        return "viewer3";
    }

    @ResponseBody
    @GetMapping("/progress")
    public Map<String,Object> progress() { return Map.of("progress", progress); }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        if (file.isEmpty()) { model.addAttribute("error", "Please select a file."); return page(model); }
        Path tmp = Files.createTempFile("its255_v3_2_", ".dat");
        file.transferTo(tmp.toFile());
        this.filePath = tmp;
        this.progress = 0.0;
        long windowBytes = Math.max(1, cfg.getWindowSizeMB()) * 1024L * 1024L;
        this.store = new ChunkedMMapRecordStore(tmp, cs(), cfg.getRecordLength(), windowBytes);
        ParallelPrefixIndexBuilder builder = new ParallelPrefixIndexBuilder(store, cfg.getPrefixIndexLength(), cfg.getIndexWorkers(), cfg.getProgressStep(), p -> this.progress = p);
        this.pidx = builder.build();
        this.filter = new FastRecordFilter(store, pidx);
        this.nav = new RecordNavigator(java.util.List.of(), -1);
        this.lastFiltered = java.util.List.of();
        return "redirect:/viewer3";
    }

    @PostMapping("/search")
    public String search(@RequestParam(name = "sccf", required = false) String sccf,
                         @RequestParam(name = "type", required = false) String type,
                         @RequestParam(name = "recNo", required = false) Integer recNo,
                         Model model) {
        if (store == null) { model.addAttribute("error", "Upload a file first."); return page(model); }
        var q = new RecordQuery(
                java.util.Optional.ofNullable(StringUtils.hasText(sccf) ? sccf.trim() : null),
                java.util.Optional.ofNullable(StringUtils.hasText(type) ? type.trim() : null),
                java.util.Optional.ofNullable(recNo));
        java.util.List<Integer> filtered = filter.apply(q);
        int startRn = q.recordNumber().orElse(-1);
        this.nav = new RecordNavigator(filtered, startRn);
        this.lastFiltered = filtered;
        return "redirect:/viewer3/current";
    }

    @GetMapping("/current")
    public String current(Model model) {
        if (store == null || nav == null || nav.size() == 0) { model.addAttribute("html", "<div class='alert alert-info'>No results. Upload a file and search.</div>"); return page(model); }
        int rn = nav.currentRecordNumber();
        String type;
        String sccf;
        try { type = (String) store.getClass().getMethod("readType", int.class).invoke(store, rn); }
        catch (Exception ex) { type = ""; }
        try { sccf = (String) store.getClass().getMethod("readSccf", int.class).invoke(store, rn); }
        catch (Exception ex) { sccf = ""; }
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>Record #").append(rn).append("  SCCF=").append(sccf).append("  Type=").append(type).append("</pre>");
        model.addAttribute("hasFile", true);
        model.addAttribute("count", nav.size());
        model.addAttribute("position", nav.position());
        model.addAttribute("hasPrev", nav.hasPrev());
        model.addAttribute("hasNext", nav.hasNext());
        model.addAttribute("meta", rn);
        model.addAttribute("html", sb.toString());
        return "viewer3";
    }

    @PostMapping("/prev") public String prev() { if (nav != null && nav.hasPrev()) nav.prev(); return "redirect:/viewer3/current"; }
    @PostMapping("/next") public String next() { if (nav != null && nav.hasNext()) nav.next(); return "redirect:/viewer3/current"; }

    @GetMapping("/export")
    public void exportZip(HttpServletResponse resp) throws Exception {
        if (store == null || lastFiltered == null || lastFiltered.isEmpty()) return;
        resp.setContentType("application/zip");
        resp.setHeader("Content-Disposition", "attachment; filename=its255_scope_export.zip");
        CsvExportService svc = new CsvExportService(store, cs(), cfg.getExportBufferSize());
        svc.exportPerTypeZip(lastFiltered, resp.getOutputStream());
    }
}
