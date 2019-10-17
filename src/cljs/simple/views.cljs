(ns simple.views
  (:require
    [reagent.core :as reagent]
    [simple.pages.booking :as booking]))


(defn main-panel
  []
  (reagent/render [booking/main-panel]
                  (.getElementById js/document "app")))




