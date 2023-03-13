(ns simpletodo.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))

;; -------------------------
;; Views

(def todos (r/atom 
            [{:desc "poke the butt" :completed true }
            {:desc "fry the garlic" :completed false}]))


(defn todo-form []
  (let [new-item (r/atom "") new-item-completed (r/atom false)]
    (fn []
      [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (swap! todos conj {:completed @new-item-completed :desc @new-item})
                           (reset! new-item "")
                           (reset! new-item-completed false)
                           )}
       [:input {:type "checkbox" 
                :value @new-item-completed
                :on-change #(reset! new-item-completed (-> % .-target .-checked))}]
       [:input {:type "text"
                :value @new-item
                :placeholder "Add new item"
                :on-change (fn [e]
                             (reset! new-item (.-value (.-target e))))}]])))
; #(reset! new-item (-> % .-target .-value))
 
(defn todo-item [todo]
  [:li {:style {:color (if (:completed todo)
                         "green"
                         "red"
                         )}} (:desc todo)])

(defn todo-list []
  [:ul
    (for [todo @todos]
      (todo-item todo))
    ])

(defn home-page []
  [:div
   [:h2 "Lists keep it simple"]
   [:p "Add new item below: "]
   [todo-form]
   [todo-list]
   ])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
