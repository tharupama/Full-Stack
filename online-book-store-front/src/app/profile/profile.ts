import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { User } from '../service/user';
import { UserProfileDto } from '../../dto/UserProfileDto';
import { ToastrService } from 'ngx-toastr';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { ProfileOrdersElements } from '../../dto/ProfileOrderElements';
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTabsModule,
    MatTableModule,
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  PROFILE_ORDERS: ProfileOrdersElements[] = [
    { orderId: 1, orderStatus: 'Hydrogen', createdAt: '1.0079', updatedAt: 'H' },
  ];
  displayedColumns: string[] = ['orderId', 'orderStatus', 'createdAt', 'updatedAt', 'moreInfo'];
  dataSource = this.PROFILE_ORDERS;

  profileForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private userService: User,
    private toaster: ToastrService,
    private cdr: ChangeDetectorRef, // ✅ 1. Inject ChangeDetectorRef
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.profileForm = this.fb.group({
      email: [{ value: '', disabled: true }],
      username: ['', [Validators.required, Validators.minLength(3)]],
      address: ['', [Validators.required]],
      mobile: ['', [Validators.required, Validators.pattern(/^\+?[\d\s\-\(\)]+$/)]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{5}(-\d{4})?$/)]],
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadUserProfile();
    } else {
      this.isLoading = false;
    }
  }

  loadUserProfile(): void {
    this.isLoading = true;

    this.userService.getUserProfile().subscribe(
      (res) => {
        // ✅ 2. Wrap in setTimeout AND call detectChanges() to completely prevent NG0100
        setTimeout(() => {
          this.profileForm.patchValue(res);
          this.isLoading = false;
          this.cdr.detectChanges(); // Forces Angular to accept the change safely

          if (isPlatformBrowser(this.platformId)) {
            this.toaster.success('User profile loaded successfully.', 'Success', { timeOut: 3000 });
          }
        });
      },
      (error) => {
        setTimeout(() => {
          console.error('Error loading user profile:', error);
          this.isLoading = false;
          this.cdr.detectChanges();

          if (isPlatformBrowser(this.platformId)) {
            this.toaster.error('Failed to load user profile.', 'Error', { timeOut: 3000 });
          }
        });
      }
    );
  }

  onSubmit(): void {
    if (this.profileForm.valid) {
      this.isLoading = true;

      // ✅ 3. CRITICAL: Use getRawValue() so the disabled 'email' field is actually sent!
      const profileData = this.profileForm.getRawValue() as UserProfileDto;

      this.userService.updateUserProfile(profileData).subscribe(
        (res) => {
          setTimeout(() => {
            this.isLoading = false;
            this.cdr.detectChanges();

            if (isPlatformBrowser(this.platformId)) {
              this.toaster.success('Profile updated successfully.', 'Success', { timeOut: 3000 });
            }
          });
        },
        (error) => {
          setTimeout(() => {
            this.isLoading = false;
            this.cdr.detectChanges();
            console.error('Error updating user profile:', error);

            if (isPlatformBrowser(this.platformId)) {
              this.toaster.error('Failed to update user profile.', 'Error', { timeOut: 3000 });
            }
          });
        }
      );
    } else {
      // Shows the red error messages on the form if the user tries to submit invalid data
      this.profileForm.markAllAsTouched();
    }
  }

  get f() {
    return this.profileForm.controls;
  }
}
