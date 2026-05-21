
# ITS255 Viewer — v3.2 Full Add‑on (Chunked MMAP, Prefix‑10 Index, Per‑Type Decoded CSV Export)

This add‑on drops into your existing Spring Boot viewer project and provides:

- **Chunked (windowed) memory mapping** for files > 2GB
- **Prefix index (len=10)** built in parallel for fast SCCF lookups
- **Strict 15‑char SCCF** matching; **prefix** search for 1..14 chars
- **Decoded CSV export** (EBCDIC/COMP‑3/BINARY) → **one CSV per Record Type** inside a **ZIP**
- **Config‑driven tuning** via `application.yml`

## Folder Layout
```
src/main/java/com/its255/viewer/...
src/main/resources/templates/viewer3.html
src/main/resources/application.yml
```

## Integration
1. Copy these folders/files into your project.
2. Ensure your project already includes:
   - `com.its255.io.Fixed255Parser` (COMP‑3/BINARY decode methods)
   - `com.its255.schema` package with `Schemas`, `RecordType`, `FieldSpec`
3. Start app (e.g., `mvn spring-boot:run`) and open **/viewer3**.

## application.yml keys
```
viewer:
  recordLength: 255            # Fixed record size
  charset: Cp037               # EBCDIC code page
  prefixIndexLength: 10        # SCCF prefix length for the index
  index:
    workers: 4                 # parallel workers to build prefix index
    progressStep: 8192         # progress update cadence
  export:
    bufferSize: 1048576        # ZIP stream buffer size (bytes)
  chunk:
    enabled: true              # turn windowed mapping on/off
    windowSizeMB: 512          # per-window map size (MB)
```

## Endpoints
- `POST /viewer3/upload`   – upload file, build index
- `GET  /viewer3/progress` – { progress: 0..1 }
- `POST /viewer3/search`   – SCCF/Type/Record# filters
- `GET  /viewer3/current`  – render current record
- `GET  /viewer3/export`   – ZIP with one CSV per record type (filtered scope)

## Notes
- The add‑on reads **record type** at COBOL pos **22..23** (2 bytes, 1‑based).
- SCCF is the **first 15 bytes**; record bytes are **never trimmed**; user input is.
- If a type has no schema, export falls back to a minimal metadata CSV.

---
