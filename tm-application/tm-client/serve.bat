@echo off
node_modules/@uisdk/cdt-package/index.js install --autofill
node_modules/@uisdk/cdt-package/index.js link tmsuserbutton
node_modules/@uisdk/cdt-serve/index.js --proxy-config proxy.config.json