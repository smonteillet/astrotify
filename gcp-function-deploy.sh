#!/bin/sh

gcloud functions deploy astrotify-function \
  --trigger-topic=astrotify-trigger \
  --entry-point=fr.astrotify.adapter.in.gcp.GCPFunctionEntrypoint \
  --region=europe-west1 \
  --runtime=java11 \
  --set-env-vars METEOBLUE_URL=https://www.meteoblue.com/en/weather/outdoorsports/seeing/muret_france_2991153,TELEGRAM_CHAT_ID=-750321988,CITY=MURET,CELESTIAL_BODY="Comète Léonard",SKY_LIVE_URL=https://theskylive.com/planetarium?localdata=43.46667%7C1.35%7CMuret+\(FR\)%7CEurope%2FParis%7C0\&obj=cometleonard\&date={date}