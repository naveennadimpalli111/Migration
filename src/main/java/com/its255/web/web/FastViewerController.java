package com.its255.web.web;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.its255.schema.Schemas;
import com.its255.viewer.ChunkedMMapRecordStore;
import com.its255.viewer.FastRecordFilter;
import com.its255.viewer.ParallelPrefixIndexBuilder;
import com.its255.viewer.PrefixIndex;
import com.its255.viewer.RecordNavigator;
import com.its255.viewer.RecordQuery;
import com.its255.viewer.SchemaHtmlRenderer;
import com.its255.viewer.ViewerConfig;
import com.its255.viewer.ViewerSession;
import com.its255.web.service.CsvExportService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/viewer")
public class FastViewerController {
  private final ViewerConfig cfg;
  

  public FastViewerController(ViewerConfig cfg) { this.cfg = cfg; }
  private Charset cs() { return Charset.forName(cfg.getCharset()); }

  @GetMapping
  public String page(Model model, HttpSession session) {
	ViewerSession vs = getSession(session);

    model.addAttribute("hasFile", vs.hasFile());
    model.addAttribute("count", (vs.nav != null) ? vs.nav.size() : 0);
    model.addAttribute("position", (vs.nav != null) ? vs.nav.position() : 0);
    model.addAttribute("hasPrev", vs.nav != null && vs.nav.hasPrev());
    model.addAttribute("hasNext", vs.nav != null && vs.nav.hasNext());
    model.addAttribute("progress", vs.progress);

    // NEW: show uploaded filename
    model.addAttribute("filename", vs.originalFilename);
    model.addAttribute("html", "");

    return "viewer";
  }

  @ResponseBody
  @GetMapping("/progress")
  public Map<String,Object> progress(HttpSession session) { 
	  
	  ViewerSession vs = getSession(session);
	  return Map.of("progress", vs.progress); 
	  
  }

  @PostMapping("/upload")
  public String upload(@RequestParam("file") MultipartFile file, Model model, HttpSession session) throws Exception {
  if (file.isEmpty()) {
        model.addAttribute("error", "Please select a file.");
        return page(model, session);
    }
    ViewerSession vs = getSession(session);

    // clean old store if present
    if (vs.store instanceof AutoCloseable ac) {
        try { ac.close(); } catch (Exception ignore) {}
    }

    Path tmp = Files.createTempFile("its255_v3_", ".dat");
    file.transferTo(tmp.toFile());

    vs.filePath = tmp;
    vs.originalFilename = file.getOriginalFilename();
    vs.progress = 0.0;

    long windowBytes = Math.max(1, cfg.getWindowSizeMB()) * 1024L * 1024L;

    vs.store = new ChunkedMMapRecordStore(
            tmp, cs(),
            cfg.getRecordLength(),
            windowBytes
    );

    ParallelPrefixIndexBuilder builder = new ParallelPrefixIndexBuilder(
            vs.store,
            cfg.getPrefixIndexLength(),
            cfg.getIndexWorkers(),
            cfg.getProgressStep(),
            p -> vs.progress = p
    );

 // If you still build index synchronously:
    vs.pidx = builder.build();
    vs.filter = new FastRecordFilter(vs.store, vs.pidx);
    vs.nav = new RecordNavigator(List.of(), -1);
    vs.lastFiltered = List.of();
    

    return "redirect:/viewer";
  }

  @PostMapping("/search")
  public String search(@RequestParam(name = "sccf", required = false) String sccf,
                       @RequestParam(name = "recordTypeCode", required = false) String type,
                       @RequestParam(name = "recNo", required = false) Integer recNo,
                       Model model, HttpSession session) {
	  ViewerSession vs = getSession(session);

	    if (!vs.hasFile()) {
	        model.addAttribute("error", "Upload a file first.");
	        return page(model, session);
	    }

	    var q = new RecordQuery(
	            Optional.ofNullable((sccf != null && !sccf.isBlank()) ? sccf.trim() : null),
	            Optional.ofNullable((type != null && !type.isBlank()) ? type.trim() : null),
	            Optional.ofNullable(recNo)
	    );

	    List<Integer> filtered = vs.filter.apply(q);
	    int startRn = q.recordNumber().orElse(-1);

	    vs.nav = new RecordNavigator(filtered, startRn);
	    vs.lastFiltered = filtered;

	    return "redirect:/viewer/current";

  }

  @GetMapping("/current")
  public String current(Model model, HttpSession session) {
	  ViewerSession vs = getSession(session);

	    if (!vs.hasFile() || vs.nav == null || vs.nav.size() == 0) {
	        model.addAttribute("html",
	            "<div class='alert alert-info'>No results. Upload a file and search.</div>");
	        return page(model, session);
	    }

	    int rn = vs.nav.currentRecordNumber();

	    String type = safeInvoke(vs.store, "readType", rn);
	    String sccf = safeInvoke(vs.store, "readSccf", rn);

	    SchemaHtmlRenderer renderer = new SchemaHtmlRenderer(vs.store, cs());
	    String tableHtml = renderer.render(rn);

	    StringBuilder sb = new StringBuilder();
	    sb.append("<pre>")
	      .append("Record #").append(rn)
	      .append(" SCCF=").append(sccf)
	      .append(" Type=").append(type.trim())
	      .append("</pre>\n")
	      .append(tableHtml)
	      .append("<pre>")
	      .append("Record #").append(rn)
	      .append(" SCCF=").append(sccf)
	      .append(" Type=").append(type.trim())
	      .append("</pre>\n");

	    model.addAttribute("hasFile", true);
	    model.addAttribute("filename", vs.originalFilename); // NEW
	    model.addAttribute("count", vs.nav.size());
	    model.addAttribute("position", vs.nav.position());
	    model.addAttribute("hasPrev", vs.nav.hasPrev());
	    model.addAttribute("hasNext", vs.nav.hasNext());
	    model.addAttribute("html", sb.toString());

	    return "viewer";

  }

  @PostMapping("/prev") 
  public String prev(HttpSession session) { 
	 ViewerSession vs = getSession(session);
	 if (vs.nav != null && vs.nav.hasPrev()) 
		 vs.nav.prev(); 
	 return "redirect:/viewer/current";
	  
  }
  @PostMapping("/next") public String next(HttpSession session) {
	  ViewerSession vs = getSession(session);
	  if (vs.nav != null && vs.nav.hasNext()) 
		  vs.nav.next(); 
	  return "redirect:/viewer/current"; }

  @GetMapping("/export")
  public void exportZip(jakarta.servlet.http.HttpServletResponse resp, HttpSession session) throws Exception {
	  	ViewerSession vs = getSession(session);
	    if (!vs.hasFile() || vs.lastFiltered == null || vs.lastFiltered.isEmpty()) return;
	
	    resp.setContentType("application/zip");
	    resp.setHeader("Content-Disposition", "attachment; filename=its255_scope_export.zip");
	
	    CsvExportService svc = new CsvExportService(
	            vs.store, cs(), cfg.getExportBufferSize());
	    svc.exportPerTypeZip(vs.lastFiltered, resp.getOutputStream());

  }
  
  private ViewerSession getSession(HttpSession session) {
	    ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");
	    if (vs == null) {
	        vs = new ViewerSession();
	        session.setAttribute("VIEWER_SESSION", vs);
	    }
	    return vs;
  }
  private String safeInvoke(Object store, String method, int rn) {
	    try {
	        return String.valueOf(store.getClass().getMethod(method, int.class).invoke(store, rn));
	    } catch (Exception e) {
	        return "";
	    }
  }
  
  @PostMapping("/clear")
  public String clear(HttpSession session) {
      ViewerSession vs = (ViewerSession) session.getAttribute("VIEWER_SESSION");
      if (vs != null) {
          try { vs.close(); } catch (Exception ignore) {}
          // delete temp file
          try { if (vs.filePath != null) Files.deleteIfExists(vs.filePath); } catch (Exception ignore) {}
          session.removeAttribute("VIEWER_SESSION");
      }
      return "redirect:/viewer";
  }
 
 
}
