#!/usr/bin/env bash
set -e

if [[ "$3" == java* && "$(id -u)" = '0' ]]; then
  echo "Switching user from root to $APP_USER..."
  chown -R "$APP_USER:$APP_GROUP" "$APP_HOME"
  exec gosu "$APP_USER" "$@"
fi

exec "$@"
