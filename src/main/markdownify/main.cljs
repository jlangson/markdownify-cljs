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
   [:div
    {:style {:display :flex}}
    [:div
     {:style {:flex "1"}}                                   ;1 is the ratio. 1:1 is equal. 2:1 gives this more size
     [:h2 "Markdown"]
     [:textarea                                             ;hashmap on html element defines attributes of <textarea>
      {:on-change #(reset! markdown (-> % .-target .-value))
       :value     @markdown
       :style { :height "500px"
               :width "100%"}}]]                  ;displays atom on reload in textbox


    [:div
     {:style {:flex "1"
              :padding-left "3em"}}
     [:h2 "HTML Preview"]
     [:div { :style {:height "500px"}
            :dangerouslySetInnerHTML {:__html (md->html @markdown)}}]]]]) ;renders md as html









(defn mount! []
  (rdom/render [app]                                        ;reagent/render deprecated
    (.getElementById js/document "app")))

(defn main! []
  (println "welcome to app")
  (mount!))

(defn reload! []
  (println "Reloaded!")
  (mount!))