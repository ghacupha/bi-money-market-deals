/*
 * Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
 * Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
const { withModuleFederationPlugin } = require('@angular-architects/module-federation/webpack');
const packageJson = require('../package.json');
// Microfrontend api, should match across gateway and microservices.
const apiVersion = '0.0.1';

const sharedDefaults = { singleton: true, strictVersion: true, requiredVersion: apiVersion };
const shareMappings = (...mappings) => Object.fromEntries(mappings.map(map => [map, { ...sharedDefaults, version: apiVersion }]));

const shareDependencies = ({ skipList = [] } = {}) =>
  Object.fromEntries(
    Object.entries(packageJson.dependencies)
      .filter(([dependency]) => !skipList.includes(dependency))
      .map(([dependency, version]) => [dependency, { ...sharedDefaults, version, requiredVersion: version }]),
  );

let sharedDependencies = shareDependencies({ skipList: ['@angular/localize', 'zone.js'] });
sharedDependencies = {
  ...sharedDependencies,
  '@angular/common/http': sharedDependencies['@angular/common'],
  'rxjs/operators': sharedDependencies.rxjs,
};

// eslint-disable-next-line no-unused-vars
module.exports = (config, options, targetOptions) => {
  return withModuleFederationPlugin({
    name: 'moneymarketbi',
    exposes: {
      './entity-navbar-items': 'app/entities/entity-navbar-items.ts',
      './entity-routes': 'app/entities/entity.routes.ts',
    },
    shared: {
      ...sharedDependencies,
      ...shareMappings(
        'app/config/input.constants',
        'app/config/pagination.constants',
        'app/config/translation.config',
        'app/core/auth',
        'app/core/config',
        'app/core/interceptor',
        'app/core/request',
        'app/core/util',
        'app/shared',
        'app/shared/alert',
        'app/shared/auth',
        'app/shared/date',
        'app/shared/language',
        'app/shared/pagination',
        'app/shared/sort',
      ),
    },
  });
};
