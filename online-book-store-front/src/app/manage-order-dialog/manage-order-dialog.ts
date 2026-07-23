import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-manage-order-dialog',
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatTableModule,
  ],
  templateUrl: './manage-order-dialog.html',
  styleUrl: './manage-order-dialog.css',
})
export class ManageOrderDialog {
  // Columns for the inner book details table
  displayedColumns: string[] = ['bookId', 'quantity'];

  // Local variable to bind to the mat-select
  selectedStatus: string;

  constructor(
    public dialogRef: MatDialogRef<ManageOrderDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    // Initialize the dropdown with the current order status
    this.selectedStatus = this.data.orderInfo.orderStatus;
  }

  onSave(): void {
    // Pass the updated data back to the parent component
    this.dialogRef.close({
      orderId: this.data.orderInfo.orderId,
      newStatus: this.selectedStatus,
    });
  }

  onCancel(): void {
    this.dialogRef.close(); // Close without saving
  }
}
