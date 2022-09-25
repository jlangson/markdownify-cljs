(ns markdownify.main
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]
            ["showdown" :as showdown]))

;must be defonce so state is saved when reloaded
(defonce markdown (reagent/atom ""))



;converter = new showdown.Converter(),
(defonce showdown-converter  (showdown/Converter.))
;text      = '# hello, markdown!', ; don't need. part of component
;html      = converter.makeHtml(text);
(defn md->html [md]
  (.makeHtml showdown-converter md))




;reagent component
(defn app []
  [:div
   [:h1 "Hello!"]
   [:textarea
    ;hashmap defines html attributes of <textarea>
   { :on-change #(reset! markdown (-> % .-target .-value))
    ;displays atom on reload in textbox
    :value @markdown}]
  [:div {:dangerouslySetInnerHTML {:__html (md->html @markdown)}}] ;renders md as html
   [:div (md->html @markdown)]                               ;shows raw conversion of md->html
   ])

















(defn mount! []
  (rdom/render [app]                                        ;reagent/render deprecated
    (.getElementById js/document "app")))

(defn main! []
  (println "welcome to app")
  (mount!))

(defn reload! []
  (println "Reloaded!")
  (mount!))