(ns amazing-larry.routes.simple-reply-coll)

;; Some of longer reply strs to keep simple-reply a little tidier.

(def techops-imgs "https://s3.amazonaws.com/com.birchbox.techops/img/")

(def hedge-img (str techops-imgs "hedge.gif"))
(def dancing-becky-img (str techops-imgs "dancingbecky.gif"))
(def pancake-bunny-img (str techops-imgs "pancake_bunny.jpg"))
(def anxious-img (str techops-imgs "anxious.gif"))
(def amazing-img (str techops-imgs "amazing.png"))
(def birch-ox-img (str techops-imgs "birch-ox.jpg"))
(def boromir-img (str techops-imgs "boromir.jpg"))

(def wtf-emojis ":alembic::pineapple::joystick::rocket::bowling::fish::umbrella::cake::unicorn_face:")

(def konami-code "^^vv<><>bastart")

(def whatfuck-img "https://i.imgur.com/NKulE5i.gif")
(def cough-img "https://media.giphy.com/media/2wTBgxQIxHDfY0Qzof/giphy.gif")

;;    MmMmM
;;   c-O.O-ↄ
;;      -
(def ascii-buzz "```   MmMmM\n  c-O.O-ↄ\n     -```")

(def techops-mihausen "https://s3.amazonaws.com/com.birchbox.techops.milhausen/")
(def milhausen
  [(str techops-mihausen "0430c8b0e927891234ba62bf2fa5c35c.jpg")
   (str techops-mihausen "303ea472fa7133f141523c2e9287232f.jpg")
   (str techops-mihausen "9bac7979810a8feb482690e55366befd.jpg")
   (str techops-mihausen "e4dad3ff14bf2d9dacd600fa6230f333.png")])

(def birchbox-logo-ascii
  "```
     :::::::::::::::::::::::::::::::::::::::::::::::::::::::
     ossssssssssssssssssssssssssssssssssssssssssssssssssssss
     ossssssssssssssssssssssssssssssssssssssssssssssssssssss
     ossssssssssssssssssssssssssssssssssssssssssssssssssssss
     osssssssoooooooooooooooo/:::/+sssssssssssssssssssssssss
     osssssso::::::::::::::::::::sssssssssssssssssssssssssss
     osssssssssss+++++++++++++++++++++++++++++++ssssssssssss
     osssssssssss-                             `oossssssssss
     osssssssssss-                              //ssssssssss
     osssssssssss-                             `ssssssssssss
     osssssssso:-`                             `ssssssssssss
     osssssssss//.                             `ssssssssssss
     osssssoooooo-                             `oooooooossss
     ossss-''''''                               '''''''':sss
     ossss+::::::`                              ::::::::+sss
     osssssssssss-                             `+++sssssssss
     osssssssssss-                              --:sssssssss
     ossssssssooo-                             `ssssssssssss
     ossssssss+++.                             `ssssssssssss
     osssssssssss-                             `ssssssssssss
     ossssssssss+.                             `ssssssssssss
     ossssssssss+.                             `ssssssssssss
     ossssssssssso++++++++++++++++++++++++++++++ssssssssssss
     osssssssoooosssssssssso----------------------------:sss
     ossssss+////+ssssssssss+++++++++++++++++++++++++++++sss
     osssssss:::::::/sssssssssssssssso++++++++++++++++ssssss
     osssssssssssssssssssssssssssssss/::::::::::::::::osssss
     ossssssssssssssssssssssssssssssssssssssssssssssssssssss
     -------------------------------------------------------
  ```")
