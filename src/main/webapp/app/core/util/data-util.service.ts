///
/// Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
/// Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Buffer } from 'buffer';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable, Observer } from 'rxjs';

export type FileLoadErrorType = 'not.image' | 'could.not.extract';

export interface FileLoadError {
  message: string;
  key: FileLoadErrorType;
  params?: any;
}

/**
 * A utility service for data.
 */
@Injectable({
  providedIn: 'root',
})
export class DataUtils {
  /**
   * Method to find the byte size of the string provides
   */
  byteSize(base64String: string): string {
    return this.formatAsBytes(this.size(base64String));
  }

  /**
   * Method to open file
   */
  openFile(data: string, contentType: string | null | undefined): void {
    contentType = contentType ?? '';

    const byteCharacters = Buffer.from(data, 'base64').toString('binary');
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], {
      type: contentType,
    });
    const fileURL = window.URL.createObjectURL(blob);
    const win = window.open(fileURL);
    win!.onload = function () {
      URL.revokeObjectURL(fileURL);
    };
  }

  /**
   * Sets the base 64 data & file type of the 1st file on the event (event.target.files[0]) in the passed entity object
   * and returns an observable.
   *
   * @param event the object containing the file (at event.target.files[0])
   * @param editForm the form group where the input field is located
   * @param field the field name to set the file's 'base 64 data' on
   * @param isImage boolean representing if the file represented by the event is an image
   * @returns an observable that loads file to form field and completes if successful
   *      or returns error as FileLoadError on failure
   */
  loadFileToForm(event: Event, editForm: FormGroup, field: string, isImage: boolean): Observable<void> {
    return new Observable((observer: Observer<void>) => {
      const eventTarget: HTMLInputElement | null = event.target as HTMLInputElement | null;
      if (eventTarget?.files?.[0]) {
        const file: File = eventTarget.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          const error: FileLoadError = {
            message: `File was expected to be an image but was found to be '${file.type}'`,
            key: 'not.image',
            params: { fileType: file.type },
          };
          observer.error(error);
        } else {
          const fieldContentType = `${field}ContentType`;
          this.toBase64(file, (base64Data: string) => {
            editForm.patchValue({
              [field]: base64Data,
              [fieldContentType]: file.type,
            });
            observer.next();
            observer.complete();
          });
        }
      } else {
        const error: FileLoadError = {
          message: 'Could not extract file',
          key: 'could.not.extract',
          params: { event },
        };
        observer.error(error);
      }
    });
  }

  /**
   * Method to convert the file to base64
   */
  private toBase64(file: File, callback: (base64Data: string) => void): void {
    const fileReader: FileReader = new FileReader();
    fileReader.onload = (e: ProgressEvent<FileReader>) => {
      if (typeof e.target?.result === 'string') {
        const base64Data: string = e.target.result.substring(e.target.result.indexOf('base64,') + 'base64,'.length);
        callback(base64Data);
      }
    };
    fileReader.readAsDataURL(file);
  }

  private endsWith(suffix: string, str: string): boolean {
    return str.includes(suffix, str.length - suffix.length);
  }

  private paddingSize(value: string): number {
    if (this.endsWith('==', value)) {
      return 2;
    }
    if (this.endsWith('=', value)) {
      return 1;
    }
    return 0;
  }

  private size(value: string): number {
    return (value.length / 4) * 3 - this.paddingSize(value);
  }

  private formatAsBytes(size: number): string {
    return `${size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')} bytes`; // NOSONAR
  }
}
