(ns l4-lp.webeditor.guifier 
  (:require [applied-science.js-interop :as jsi]
            [l4-lp.swipl.js.wasm-query :as swipl-wasm-query]
            [l4-lp.syntax.l4-to-prolog :as l4->prolog]
            [promesa.core :as prom]
            [shadow.esm :refer [dynamic-import]]))

(def ^:private guifier
  {:element-id "guifier"
   :constructor
   (prom/let
    [cdn-url "https://cdn.jsdelivr.net/npm/guifier@1.0.24/dist/Guifier.js"
     mod (dynamic-import cdn-url)]
     (jsi/get mod :default))})

(def ^:private query
  (-> "query" l4->prolog/l4->prolog-str))

(defn query-and-trace-and-guifier! [l4-program]
  (-> js/document
      (jsi/call :getElementById (:element-id guifier))
      (jsi/assoc! :innerHTML ""))

  (prom/let
   [program (-> l4-program l4->prolog/l4-program->prolog-program-str)

    _ (jsi/call js/console :log "Transpiled program: " program)
    _ (jsi/call js/console :log "Transpiled query: " query)

    stack-trace (swipl-wasm-query/query-and-trace-js! program query)

    Guifier (:constructor guifier)]
    (Guifier. #js {:data stack-trace
                   :dataType "js"
                   :elementSelector (str "#" (:element-id guifier))
                   :withoutContainer true
                   :readOnlyMode true})))