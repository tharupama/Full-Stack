import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { BookUpdateDto } from '../../dto/BookUpdateDto';

@Component({
  selector: 'app-book-update-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
  ],
  templateUrl: './book-update-modal.html',
})
export class BookUpdateModal {
  updateForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<BookUpdateModal>,
    @Inject(MAT_DIALOG_DATA) public data: BookUpdateDto,
    private fb: FormBuilder
  ) {
    // Initialize form with ALL fields from the Java Entity
    this.updateForm = this.fb.group({
      id: data.id,
      title: [data.title, Validators.required],
      imgUrl: [data.imgUrl || ''],
      category: [data.category || ''],
      author: [data.author, Validators.required],
      description: [data.description || ''],
      price: [data.price, [Validators.required, Validators.min(0)]],
      stock: [data.stock, [Validators.required, Validators.min(0)]],
      isAvailable: [data.isAvailable !== undefined ? data.isAvailable : true],
    });
  }

  onSubmit() {
    if (this.updateForm.valid) {
      // This now returns an object with ALL fields, no undefined values!
      this.dialogRef.close(this.updateForm.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
