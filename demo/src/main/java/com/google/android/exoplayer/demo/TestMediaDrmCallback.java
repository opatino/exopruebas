/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer.demo;

import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.google.android.exoplayer.util.Util;

import android.annotation.TargetApi;
import android.media.MediaDrm.KeyRequest;
import android.media.MediaDrm.ProvisionRequest;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A {@link MediaDrmCallback} for test content.
 */
@TargetApi(18)
/* package */ final class TestMediaDrmCallback implements MediaDrmCallback {

  private static final String WIDEVINE_BASE_URL = "https://proxy.uat.widevine.com/proxy";
  private static final String PLAYREADY_BASE_URL =
      "http://playready.directtaps.net/pr/svc/rightsmanager.asmx";
  private static final Map<String, String> PLAYREADY_KEY_REQUEST_PROPERTIES;
  static {
    HashMap<String, String> keyRequestProperties = new HashMap<>();
    keyRequestProperties.put("Content-Type", "text/xml");
    keyRequestProperties.put("SOAPAction",
        "http://schemas.microsoft.com/DRM/2007/03/protocols/AcquireLicense");
    PLAYREADY_KEY_REQUEST_PROPERTIES = keyRequestProperties;
  }

  private final String defaultUrl;
  private final Map<String, String> keyRequestProperties;

  public static TestMediaDrmCallback newWidevineInstance(String contentId, String provider) {
    String defaultUrl = WIDEVINE_BASE_URL + "?video_id=" + contentId + "&provider=" + provider;
    return new TestMediaDrmCallback(defaultUrl, null);
  }

  public static TestMediaDrmCallback newPlayReadyInstance() {
    return new TestMediaDrmCallback(PLAYREADY_BASE_URL, PLAYREADY_KEY_REQUEST_PROPERTIES);
  }

  private TestMediaDrmCallback(String defaultUrl, Map<String, String> keyRequestProperties) {
    this.defaultUrl = defaultUrl;
    this.keyRequestProperties = keyRequestProperties;
  }

  @Override
  public byte[] executeProvisionRequest(UUID uuid, ProvisionRequest request) throws IOException {
    String url = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
    return Util.executePost(url, null, null);
  }

  @Override
  public byte[] executeKeyRequest(UUID uuid, KeyRequest request) throws Exception {
    String url = request.getDefaultUrl();
    if (TextUtils.isEmpty(url)) {
      url = defaultUrl;
    }
    return Util.executePost(url, request.getData(), keyRequestProperties);
  }

}