#!/usr/bin/env bash
[[ -r "$HOME/.nvm/nvm.sh" ]] && source "$HOME/.nvm/nvm.sh"
node_modules/@uisdk/cdt-serve/index.js --proxy-config proxy.config.json
