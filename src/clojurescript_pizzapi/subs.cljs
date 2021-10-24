(ns clojurescript-pizzapi.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::price
 (fn [db]
   (:price (:pizzapi db))))

(re-frame/reg-sub
 ::diameter
 (fn [db]
   (:diameter (:pizzapi db))))

(re-frame/reg-sub
 ::relative-price-fixed
 (fn [db]
   (:relative-price-fixed (:pizzapi db))))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
