#!/bin/sh

gcloud functions deploy astrotify-function \
  --trigger-topic=astrotify-trigger \
  --entry-point=fr.astrotify.adapter.in.gcp.GCPFunctionEntrypoint \
  --region=europe-west1 \
  --runtime=java11