/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.util.Assertions;

import java.util.Arrays;

/**
 * Defines a track selection.
 */
public final class TrackSelection {

  /**
   * The index of the selected {@link TrackGroup}.
   */
  public final int group;
  /**
   * The number of selected tracks within the {@link TrackGroup}. Always greater than zero.
   */
  public final int length;

  private final int[] tracks;

  // Lazily initialized hashcode.
  private int hashCode;

  /**
   * @param group The index of the {@link TrackGroup}.
   * @param tracks The indices of the selected tracks within the {@link TrackGroup}. Must not be
   *     null or empty.
   */
  public TrackSelection(int group, int... tracks) {
    Assertions.checkState(tracks.length > 0);
    this.group = group;
    this.tracks = tracks;
    this.length = tracks.length;
  }

  /**
   * Gets the index of the selected track at a given index in the selection.
   *
   * @param index The index in the selection.
   * @return The index of the selected track.
   */
  public int getTrack(int index) {
    return tracks[index];
  }

  /**
   * Gets a copy of the individual track indices.
   *
   * @return The track indices.
   */
  public int[] getTracks() {
    return tracks.clone();
  }

  /**
   * Gets whether a given track index is included in the selection.
   *
   * @param trackIndex The track index.
   * @return True if the index is included in the selection. False otherwise.
   */
  public boolean containsTrack(int trackIndex) {
    for (int i = 0; i < length; i++) {
      if (tracks[i] == trackIndex) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      hashCode = 31 * group + Arrays.hashCode(tracks);
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TrackSelection other = (TrackSelection) obj;
    return group == other.group && Arrays.equals(tracks, other.tracks);
  }

}