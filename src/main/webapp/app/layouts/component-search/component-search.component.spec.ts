import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComponentSearchComponent } from './component-search.component';

describe('ComponentSearchComponent', () => {
  let component: ComponentSearchComponent;
  let fixture: ComponentFixture<ComponentSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComponentSearchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ComponentSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
