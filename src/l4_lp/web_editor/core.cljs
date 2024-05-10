(ns l4-lp.web-editor.core
  (:require [applied-science.js-interop :as jsi]
            [hoplon.core :as h]
            [hoplon.goog]
            [l4-lp.web-editor.codemirror-editor :refer [bind-editor!]]))

(def ^:private initial-editor-text
  ";; Enter an L4 program here, and then press M-Enter to evaluate the query.
;; When the evaluation completes, an execution trace will appear below the input window.
;; Note that the whole pipeline, from parsing and transpilation to evaluation and
;; processing of traces, runs directly in the browser.

GIVETH x
QUERY MIN (MINUS 0 x) (SUM [1 2 3 -12]) 2 < PRODUCT (MAX (DIVIDE 12 3) 4 -1) 19 x

GIVEN x y
QUERY SUM x y IS 0
AND MINUS x y IS 0

GIVEN (x IS A Number)
DECIDE x is between 0 and 10 or is 100
IF 0 <= x AND x <= 10
OR x IS MAX 100 -20

DECIDE (2023 - 1 - 10) is a date

GIVEN (xs IS A LIST OF Number)
GIVETH x
DECIDE b of x and xs
WHERE x IS SUM xs

DECIDE b of 0 and _ OTHERWISE")

(def ^:private web-editor-app-id
  "web-editor-app")

(h/defelem ^:private html [_attrs _children]
  (h/div
   (h/link :rel "stylesheet"
           :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
           :integrity "sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
           :crossorigin "anonymous")

   (h/title "L4 web editor")
   (h/h1 "L4 web editor")

   (h/a :href "https://github.com/smucclaw/l4-lp"
        "Click here to visit the project on GitHub!")

   (h/br)
   (h/br)

  ;;  (h/div :class "form-group"
  ;;         (h/label :for "l4-program"
  ;;                  :class "col-sm-1 control-label"
  ;;                  (h/b "L4 Program"))
  ;;         (h/div :id "editor"))

   (h/div :id "editor")

   (h/br)

   (h/div :id "guifier")))

(defn mount-components! []
  (-> js/document
      (jsi/call :getElementById web-editor-app-id)
      (jsi/call :replaceChildren (html))))

(defn start! []
  (jsi/call js/console :log "Starting..."))

(defn stop! []
  (jsi/call js/console :log "Stopping..."))

(defn init! []
  (mount-components!)
  (bind-editor! initial-editor-text)
  (start!))