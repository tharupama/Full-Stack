import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageOrderDialog } from './manage-order-dialog';

describe('ManageOrderDialog', () => {
  let component: ManageOrderDialog;
  let fixture: ComponentFixture<ManageOrderDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageOrderDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageOrderDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
