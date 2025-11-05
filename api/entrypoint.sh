#!/bin/sh
inotifywait -m /root/bots -e create |
while read path action file; do
    chmod +x "$path/$file"
done &

exec java -jar /app/awale-arena-0.0.1-SNAPSHOT.jar \
     --supabase.user="${SUPABASE_USER}" \
     --supabase.password="${SUPABASE_PASSWORD}" \
     --cors.allowed.origins="${CORS_ALLOWED_ORIGINS}" \
     --sandbox.command="${SANDBOX_COMMAND}" \
     --java.path="${JAVA_PATH}" \
     --python.path="${PYTHON_PATH}"