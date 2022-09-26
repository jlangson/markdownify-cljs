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


;; https://hackernoon.com/copying-text-to-clipboard-with-javascript-df4d4988697f
(defn copy-to-clipboard [s]
  (let [el (.createElement js/document "textarea")
        selected (when (pos? (-> js/document .getSelection .-rangeCount))
                   (-> js/document .getSelection (.getRangeAt 0)))]
    (set! (.-value el) s)
    (.setAttribute el "readonly" "")
    (set! (-> el .-style .-position) "absolute")
    (set! (-> el .-style .-left) "-9999px")
    (-> js/document .-body (.appendChild el))
    (.select el)
    (.execCommand js/document "copy")
    (-> js/document .-body (.removeChild el))
    (when selected
      (-> js/document .getSelection .removeAllRanges)
      (-> js/document .getSelection (.addRange selected)))))


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
       :style     {:height "500px"
                   :width  "100%"}}]
     [:button
      {:on-click #(copy-to-clipboard @markdown)}
      "Copy Markdown"]]

    [:div
     {:style {:flex "1"
              :padding-left "3em"}}
     [:h2 "HTML Preview"]
     [:div {:style                   {:height "500px"}
            :dangerouslySetInnerHTML {:__html (md->html @markdown)}}]
     [:button
      {:on-click #(copy-to-clipboard (md->html @markdown))}
      "Copy HTML!!!"]]]])



(defn mount! []
  (rdom/render [app]                                        ;reagent/render deprecated
    (.getElementById js/document "app")))

(defn main! []
  (println "welcome to app")
  (mount!))

(defn reload! []
  (println "Reloaded!")
  (mount!))