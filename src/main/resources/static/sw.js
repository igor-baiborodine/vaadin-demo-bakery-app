'use strict';

// Important: update the version each time you change any of the files listed below
const version = 5;
// define your offline-page and assets used by it
const offlinePage = 'offline-page.html';
const offlineAssets = [offlinePage, 'manifest.json',
  'images/offline-login-banner.jpg'
];

const CACHE_NAME = 'static-v' + version;

function createCacheBustedRequest(url) {
  const request = new Request(url, {cache: 'reload'});
  // See https://fetch.spec.whatwg.org/#concept-request-mode
  // This is not yet supported in Chrome as of M48, so we need to explicitly check to see
  // if the cache: 'reload' option had any effect.
  if ('cache' in request) {
    return request;
  }

  // If {cache: 'reload'} didn't have any effect, append a cache-busting URL parameter instead.
  const bustedUrl = new URL(url, self.location.href);
  bustedUrl.search += (bustedUrl.search ? '&' : '') + 'cachebust=' + Date.now();
  return new Request(bustedUrl);
}


function cacheUrl(url) {
  return fetch(createCacheBustedRequest(url))
    .then(response => caches.open(CACHE_NAME)
      .then(cache => cache.put(url, response)));
}

self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (CACHE_NAME !== cacheName) {
            // If this cache name isn't present in the array of "expected" cache names,
            // then delete it.
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});

self.addEventListener('install', function(event) {
  event.waitUntil(Promise.all(
    offlineAssets.map(url => cacheUrl(url))
  ));
});

const doesRequestAcceptHtml = function(request) {
  return request.headers.get('Accept')
    .split(',')
    .some(type => type === 'text/html');
};

self.addEventListener('fetch', function(event) {
  const request = event.request;
  if (doesRequestAcceptHtml(request)) {
    // HTML pages fallback to offline page
    event.respondWith(
      fetch(request)
        .catch(() => caches.match(offlinePage))
    );
  } else {
    if (request.cache === 'only-if-cached' && request.mode !== 'same-origin') {
      return;
    }
    event.respondWith(
      caches.match(request)
        .then(response => response || fetch(request))
    );
  }
});
