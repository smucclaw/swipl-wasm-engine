(ns l4-lp.web-editor.query-button 
  (:require ["@mui/icons-material/Send$default" :as SendIcon]
            ["@mui/lab/LoadingButton$default" :as LoadingButton]
            [applied-science.js-interop :as jsi]
            [l4-lp.swipl.js.wasm-query :as swipl-wasm-query]
            [l4-lp.syntax.l4-to-prolog :as l4->prolog]
            [promesa.core :as prom]
            [uix.core :as uix]))

(uix/defui query-button
  [{:keys [cm-editor-ref
           queries-running-state
           set-prolog!
           set-query-results!]}]
  (let [[queries-running? set-queries-running!] queries-running-state
        query-fn
        (fn []
          (set-queries-running! true)
          (set-query-results! [])
          (set-prolog! nil)

          (let [cm-editor-doc (jsi/get-in @cm-editor-ref [:view :state :doc])
                prolog-program-and-queries
                (-> cm-editor-doc str l4->prolog/l4->prolog-program+queries)]
            (prom/do
              (set-prolog! prolog-program-and-queries)

              (swipl-wasm-query/query-and-trace!
               prolog-program-and-queries
               (fn [result] (set-query-results! #(conj % result))))

              (set-queries-running! false))))]

    (uix/$ LoadingButton
           {:sx #js {:ml 3}
            :loading queries-running?
            :variant :contained
            :color :inherit
            :size :medium
            :end-icon (uix/$ SendIcon)
            :on-click query-fn}
           "Run Queries")))