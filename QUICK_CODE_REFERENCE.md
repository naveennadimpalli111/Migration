# Quick Code Reference - Delete/Edit Implementation

## 1️⃣ ViewerSession.java

### Location: `src/main/java/com/its255/viewer/ViewerSession.java`

**Add these imports at top** (after existing imports):
```java
import java.util.HashSet;
import java.util.Set;
```

**Add this field** (after `editOverlay` field):
```java
public Set<Integer> deletedRecords = new HashSet<>();
```

**Context**:
```java
public Map<Integer,Map<String,String>> editOverlay = new HashMap<>();
public Set<Integer> deletedRecords = new HashSet<>();  // ← NEW
@Override
public void close() {
    // ...
}
```

---

## 2️⃣ FastViewerController.java

### Location: `src/main/java/com/its255/web/web/FastViewerController.java`

**Add import** (near top):
```java
import java.util.ArrayList;
```

**Update search() method** (around line 170):
Find this:
```java
List<Integer> filtered = vs.filter.apply(q);
vs.selectedRecordType = type;
```

Replace with:
```java
List<Integer> filtered = vs.filter.apply(q);
// Remove any deleted records from results
if (vs.deletedRecords != null && !vs.deletedRecords.isEmpty()) {
    filtered = new ArrayList<>(filtered);
    filtered.removeIf(vs.deletedRecords::contains);
}
vs.selectedRecordType = type;
```

**Add new delete endpoint** (after search() method, around line 560):
```java
@PostMapping("/delete")
public void deleteHorizontalRecord(@RequestParam("recordNo") int recordNo,
        HttpSession session, jakarta.servlet.http.HttpServletResponse response) throws IOException {
    ViewerSession vs = getSession(session);
    if (vs.deletedRecords == null) {
        vs.deletedRecords = new java.util.HashSet<>();
    }
    vs.deletedRecords.add(recordNo);
    if (vs.lastFiltered != null) {
        vs.lastFiltered = new ArrayList<>(vs.lastFiltered);
        vs.lastFiltered.removeIf(r -> r == recordNo);
    }
    response.setStatus(200);
    response.getWriter().write("OK");
}
```

---

## 3️⃣ SchemaHtmlRenderer.java

### Location: `src/main/java/com/its255/viewer/SchemaHtmlRenderer.java`

**In renderHorizontalPage() method**, find the header row building (around line 230-240):

Find this:
```java
sb.append("<th style='min-width:180px;'>Record No</th>");
sb.append("<th style='min-width:180px;'>SCCF ID</th>");
sb.append("<th style='min-width:120px;'>Type</th>");

List<FieldSpec> headerLayout = SchemaRegistry.getSchema(transactionType,
```

Replace with:
```java
sb.append("<th style='min-width:180px;'>Record No</th>");
sb.append("<th style='min-width:180px;'>SCCF ID</th>");
sb.append("<th style='min-width:120px;'>Type</th>");
sb.append("<th style='min-width:160px;'>Actions</th>");

List<FieldSpec> headerLayout = SchemaRegistry.getSchema(transactionType,
```

**In the row building loop**, find this (around line 280):
```java
// Normalize special placeholder '{' to '0' for display
sccf = normalizeValue(sccf);
sb.append("<td>").append(escape(sccf)).append("</td>");

sb.append("<td>").append(escape(displayType)).append("</td>");

if (layout != null) {
```

Replace with:
```java
// Normalize special placeholder '{' to '0' for display
sccf = normalizeValue(sccf);
sb.append("<td>").append(escape(sccf)).append("</td>");

sb.append("<td>").append(escape(displayType)).append("</td>");

// Actions column with Edit and Delete buttons
sb.append("<td>");
sb.append("<button type='button' class='btn btn-sm btn-outline-primary me-1' onclick='editHorizontalRecord(").append(recordNo).append(")'>");
sb.append("<i class='bi bi-pencil'></i> Edit");
sb.append("</button>");
sb.append("<button type='button' class='btn btn-sm btn-outline-danger' onclick='deleteHorizontalRecord(").append(recordNo).append(")'>");
sb.append("<i class='bi bi-trash'></i> Delete");
sb.append("</button>");
sb.append("</td>");

if (layout != null) {
```

---

## 4️⃣ viewer.html

### Location: `src/main/resources/templates/viewer.html`

**Add Bootstrap Icons CDN** (in `<head>`, after Bootstrap CSS):
```html
<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" />
```

**Add JavaScript functions** (near end of file, before closing `</script>` tag):

Find:
```javascript
    document.addEventListener("DOMContentLoaded", function () {
      const saveBtn = document.getElementById("btnSave");
      const saveForm = document.getElementById("saveForm");
      if (saveBtn && saveForm) {
        saveBtn.addEventListener("click", function (e) {
          e.preventDefault();
          console.log("Submitting form....");
          saveForm.submit();
        });
      }
    })

  </script>
```

Replace with:
```javascript
    document.addEventListener("DOMContentLoaded", function () {
      const saveBtn = document.getElementById("btnSave");
      const saveForm = document.getElementById("saveForm");
      if (saveBtn && saveForm) {
        saveBtn.addEventListener("click", function (e) {
          e.preventDefault();
          console.log("Submitting form....");
          saveForm.submit();
        });
      }
    })

    // Delete record from horizontal view with confirmation
    function deleteHorizontalRecord(recordNo) {
      if (!confirm("Delete record #" + recordNo + "?")) {
        return;
      }

      // Send delete request to backend
      fetch("/viewer/delete", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "recordNo=" + recordNo
      })
        .then(response => {
          if (response.ok) {
            // Find and remove row from DOM
            const row = document.querySelector("tr[data-recordno='" + recordNo + "']");
            if (row) {
              row.remove();
            }

            // Check if table body is empty
            const tbody = document.getElementById("horizontalTbody");
            if (tbody && tbody.children.length === 0) {
              // Replace table with "No Records Found" message
              const horizontalView = document.getElementById("horizontalView");
              if (horizontalView) {
                horizontalView.innerHTML = "<div class='text-center text-muted p-5'><p>No Records Found</p></div>";
              }
            }

            // Maintain horizontal view mode
            sessionStorage.setItem("viewMode", "horizontal");
            const viewModeInput = document.getElementById("viewMode");
            if (viewModeInput) {
              viewModeInput.value = "horizontal";
            }
          }
        })
        .catch(err => console.error("Error deleting record:", err));
    }

    // Edit record in horizontal view (placeholder - can be expanded)
    function editHorizontalRecord(recordNo) {
      console.log("Edit record #" + recordNo);
      // Scroll to record and focus on edit inputs if available
      const row = document.querySelector("tr[data-recordno='" + recordNo + "']");
      if (row) {
        row.scrollIntoView({ behavior: "smooth", block: "center" });
        // Find first input in row and focus
        const firstInput = row.querySelector("input");
        if (firstInput) {
          firstInput.focus();
        }
      }
    }

  </script>
```

---

## Verification Checklist

- [ ] ViewerSession.java: Added imports ✓
- [ ] ViewerSession.java: Added deletedRecords field ✓
- [ ] FastViewerController.java: Added ArrayList import ✓
- [ ] FastViewerController.java: Updated search() with filter ✓
- [ ] FastViewerController.java: Added @PostMapping("/delete") ✓
- [ ] SchemaHtmlRenderer.java: Added Actions column header ✓
- [ ] SchemaHtmlRenderer.java: Added Delete button ✓
- [ ] SchemaHtmlRenderer.java: Added Edit button ✓
- [ ] viewer.html: Added Bootstrap Icons CDN ✓
- [ ] viewer.html: Added deleteHorizontalRecord() function ✓
- [ ] viewer.html: Added editHorizontalRecord() function ✓
- [ ] No compilation errors ✓
- [ ] Test all 12 scenarios ✓

---

## How to Deploy

1. **Update Files**: Make changes listed above in each file
2. **Compile**: Run `mvn clean compile -DskipTests`
3. **Verify**: Check for zero compilation errors
4. **Package**: Run `mvn package` to build WAR/JAR
5. **Deploy**: Copy to application server
6. **Test**: Run through test scenarios

---

## Quick Test

1. Upload a file with transaction type SF
2. Search for record type 05 (should show multiple results)
3. Switch to Horizontal View
4. Click Delete button on first record
5. Confirm in popup
6. Verify row removed and no page refresh
7. Delete last remaining record
8. Verify "No Records Found" message appears
9. Refresh page - verify view mode is still horizontal
10. Search again - verify deleted records not in results

✅ **All tests pass? Implementation is successful!**
