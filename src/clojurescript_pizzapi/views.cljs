(ns clojurescript-pizzapi.views
  (:require
   [re-frame.core :as re-frame]
   [clojurescript-pizzapi.events :as events]
   [clojurescript-pizzapi.routes :as routes]
   [clojurescript-pizzapi.subs :as subs]))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])
        price (re-frame/subscribe [::subs/price])
        diameter (re-frame/subscribe [::subs/diameter])
        relative-price-fixed (re-frame/subscribe [::subs/relative-price-fixed])]
    [:div
     [:h1
      (str "Hello from " @name ". This is the Home Page.")]

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "go to About Page"]]
     [:hr]
     [:div
      [:label "Price"]
      [:input {:type "text"
               :value (str @price)
               :on-change
               #(re-frame/dispatch [::events/price-change "price" (-> % .-target .-value)])}]]
     [:div
      [:label "Diameter"]
      [:input {:type "text"
               :value (str @diameter)
               :on-change
               #(re-frame/dispatch [::events/diameter-change "diameter" (-> % .-target .-value)])}]]
     [:div
      [:p (str @relative-price-fixed " Euros per m²")]]]))

(defmethod routes/panels :home-panel [] [home-panel])

;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:on-click #(re-frame/dispatch [::events/navigate :home])}
     "go to Home Page"]]])

(defmethod routes/panels :about-panel [] [about-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
