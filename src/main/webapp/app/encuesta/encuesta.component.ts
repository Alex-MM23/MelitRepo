import { Component } from '@angular/core';
import { FormGroup, FormControl, FormsModule, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-encuesta',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './encuesta.component.html',
  styleUrl: './encuesta.component.scss'
})
export class EncuestaComponent {
  formView: boolean= false;

  editForm = new FormGroup({
    nombre: new FormControl ('', Validators.required),
    edad: new FormControl (0, Validators.required),
    sexo: new FormControl ('', Validators.required),
    puntuacion: new FormControl (0, Validators.required),
    comentarios: new FormControl (''),
  });

  constructor(private activeModal: NgbActiveModal) {
    this.editForm.get('puntuacion')?.valueChanges.subscribe(value => {
      this.editForm.get('puntuacion')?.setValue(value, { emitEvent: false });
    });

    this.editForm.get('puntuacionNumber')?.valueChanges.subscribe(value => {
      this.editForm.get('puntuacionNumber')?.setValue(value, { emitEvent: false });
    });
  }

  closeModal() {
    this.activeModal.close();
  }

  submitEncuesta(){
    if(this.editForm && this.editForm.valid){
      console.log(this.editForm.value);
    }
    this.closeModal();
  }
}
