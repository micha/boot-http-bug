(set-env!
 :source-paths    #{"src" "task-src"}
 :resource-paths  #{"html"})

(require
 '[pandeiro.boot-http :refer [serve]])

(require
 '[clojure.java.io :as io]
 '[clojure.string :as s])

(defn update-html [file-name]
  (let [tmp (tmp-dir!)]
    (with-pre-wrap fileset
      (let [f (io/resource file-name)]
        (spit (io/file tmp file-name) (s/replace (slurp f) #"h1" "h4"))
        (-> fileset
            (add-resource tmp)
            commit!)))))

(defn inspect-resource
  "Output resource URL and content as it exists in Boot fileset"
  [resource-path & [label]]
  (with-pre-wrap fileset
    (println (.toString (io/resource resource-path)))
    (println label " :: "(slurp (io/resource resource-path)))
    fileset))

(deftask dev-watch-working []
  (comp
   (watch)
   (inspect-resource "foo.html")
   (update-html "foo.html")
   (inspect-resource "foo.html")
   (serve :silent true :port 8080)
   (inspect-resource "foo.html")))

(deftask dev-once-broken []
  (comp
   (inspect-resource "foo.html" "before update")
   (update-html "foo.html")
   (inspect-resource "foo.html" "after update")
   (serve :silent true :port 8080)
   (inspect-resource "foo.html" "after serve")
   (wait)))


