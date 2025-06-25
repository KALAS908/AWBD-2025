import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { RouterModule } from '@angular/router';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzCheckboxModule } from 'ng-zorro-antd/checkbox';
import { NzFormModule, NzFormTooltipIcon } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzCommentModule } from 'ng-zorro-antd/comment';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ReactiveFormsModule } from '@angular/forms';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzPaginationModule } from 'ng-zorro-antd/pagination';
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    NzButtonModule,
    NzIconModule,
    NzLayoutModule,
    NzMenuModule,
    NzBreadCrumbModule,
    NzFormModule,
    ReactiveFormsModule,
    NzCheckboxModule,
    NzInputModule,
    NzSelectModule,
    NzTableModule,
    NzPaginationModule,
    NzCommentModule,

    // Import RouterModule if you need routing capabilities
  ],
  exports: [
    CommonModule,
    NzMenuModule,
    NzButtonModule,
    NzIconModule,
    NzLayoutModule,
    NzBreadCrumbModule,
    NzFormModule,
    ReactiveFormsModule,
    NzCheckboxModule,
    NzInputModule,
    NzSelectModule,
    NzTableModule,
    NzPaginationModule,
    NzCommentModule,
  ],
})
export class MaterialModule {
  // This module can be used to import and export Angular Material components
  // and other shared modules that you want to use across your application.
}
