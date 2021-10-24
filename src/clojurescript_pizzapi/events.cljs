(ns clojurescript-pizzapi.events
  (:require
   [re-frame.core :as re-frame]
   [clojurescript-pizzapi.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn nan-is-0 [number]
  (let [numberF (js/parseFloat number)]
    (if (js/isNaN numberF)
      0
      numberF)))

(defn update-area [val]
  (let [diameter (nan-is-0 val)]
    (nan-is-0 (* Math/PI (Math/pow (/ diameter 2) 2)))))

(defn update-relative-price [area price]
  (let [a (nan-is-0 area)
        p (nan-is-0 price)
        factor (/ 1 (/ a 10000))]
    (->
     (js/parseFloat  (* factor p) 2)
     nan-is-0);
    ))

(defn calculate-pizza [db values]
  (let [diameter (or (:diameter values) (:diameter db))
        price (or (:price values) (:price db))
        area (update-area diameter)
        relative-price (update-relative-price area price)
        relative-price-fixed (.toFixed relative-price 2)]
    {:price price
     :diameter diameter
     :area area
     :relative-price relative-price
     :relative-price-fixed relative-price-fixed}))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-fx
 ::navigate
 (fn-traced [_ [_ handler]]
            {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
            {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-fx
 ::price-change
 (fn-traced [cofx [_ _ val]]
            (let [pizzapi (calculate-pizza (:pizzapi (:db cofx)) {:price val})]
              {:db (assoc (:db cofx)
                          :pizzapi pizzapi)})))

(re-frame/reg-event-fx
 ::diameter-change
 (fn-traced [cofx [_ _ val]]
            (let [pizzapi (calculate-pizza (:pizzapi (:db cofx)) {:diameter val})]
              {:db (assoc (:db cofx)
                          :pizzapi pizzapi)})))