package com.its255.web.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.its255.schema.RecordType;
import com.its255.web.service.ViewerService;
import com.its255.web.service.ViewerService.SortBy;
import com.its255.web.service.ViewerService.SortDir;

@Controller
@Validated
public class UploadController {

    private final ViewerService viewer;

    public UploadController(ViewerService viewer) {
        this.viewer = viewer;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("types", viewer.supportedTypes());
        return "index";
    }

    @PostMapping("/preview")
    public String preview(MultipartFile file,
                          String recordTypeCode, // optional now (ignored if recordNumber given)
                          String charset,        // optional (default Cp037)
                          Integer maxRows,       // optional
                          Integer recordNumber,  // NEW: 1-based record index
                          String q,              // NEW: filter
                          String sortBy,         // NEW: julian|serial|none
                          String sortDir,        // NEW: asc|desc
                          Model model) throws IOException {

        if (file == null || file.isEmpty()) {
            model.addAttribute("error", "Please upload a raw EBCDIC file (fixed 255 bytes per record).");
            return index(model);
        }

        // Determine requested type ONLY if recordNumber not provided
        RecordType rt = RecordType.UNKNOWN;
        if (recordNumber == null || recordNumber <= 0) {
            if (recordTypeCode == null || recordTypeCode.isBlank()) {
                model.addAttribute("error", "Choose a record type or enter a record number.");
                return index(model);
            }
            rt = switch (recordTypeCode) {
            	case "05" -> RecordType.RT_05;
                case "10" -> RecordType.RT_10;
                case "15" -> RecordType.RT_15;
                case "20" -> RecordType.RT_20;
                case "30" -> RecordType.RT_30;
                case "31" -> RecordType.RT_31;
                case "32" -> RecordType.RT_32;
                case "33" -> RecordType.RT_33;
                case "40" -> RecordType.RT_40;
                case "41" -> RecordType.RT_41;
                case "42" -> RecordType.RT_42;
                case "43" -> RecordType.RT_43;
                case "44" -> RecordType.RT_44;
                case "45" -> RecordType.RT_45;
                case "46" -> RecordType.RT_46;
                case "47" -> RecordType.RT_47;
                case "50" -> RecordType.RT_50;
                case "60" -> RecordType.RT_60;
                case "65" -> RecordType.RT_65;
                case "66" -> RecordType.RT_66;
                case "71" -> RecordType.RT_71;
                case "72" -> RecordType.RT_72;
                case "73" -> RecordType.RT_73;
                case "74" -> RecordType.RT_74;
                case "80" -> RecordType.RT_80;
                case "90" -> RecordType.RT_90;
                case "9D" -> RecordType.RT_9D;
                default -> RecordType.UNKNOWN;
            };
            if (rt == RecordType.UNKNOWN) {
                model.addAttribute("error", "Unsupported record type code: " + recordTypeCode);
                return index(model);
            }
        }

        // Parse sort params
        SortBy sBy = (sortBy == null || sortBy.isBlank()) ? SortBy.NONE : switch (sortBy.toLowerCase()) {
            case "julian" -> SortBy.JULIAN;
            case "serial" -> SortBy.SERIAL;
            default -> SortBy.NONE;
        };
        SortDir sDir = (sortDir == null || sortDir.isBlank()) ? SortDir.ASC :
                ("desc".equalsIgnoreCase(sortDir) ? SortDir.DESC : SortDir.ASC);

        ViewerService.TableResult result = viewer.preview(
                file, charset, rt, maxRows, recordNumber, q, sBy, sDir);

        model.addAttribute("result", result);
        model.addAttribute("label", (recordNumber != null && recordNumber > 0)
                ? ("Record #" + recordNumber + " � " + viewer.labelOf(result.recordType()))
                : viewer.labelOf(result.recordType()));
        return "preview";
    }
}

