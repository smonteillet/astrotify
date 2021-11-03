#!/bin/sh

gcloud functions deploy astrotify-function \
  --trigger-topic=astrotify-trigger \
  --entry-point=fr.astrotify.gcp.GCPEntrypoint \
  --region=europe-west1 \
  --runtime=java11 \
  --set-env-vars METEOBLUE_URL=https://www.meteoblue.com/en/weather/outdoorsports/seeing/muret_france_2991153,TELEGRAM_CHAT_ID=-750321988