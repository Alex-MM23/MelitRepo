import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMarca } from 'app/entities/marca/marca.model';
import { MarcaService } from 'app/entities/marca/service/marca.service';
import { IModelo } from 'app/entities/modelo/modelo.model';
import { ModeloService } from 'app/entities/modelo/service/modelo.service';
import { CocheService } from '../service/coche.service';
import { ICoche } from '../coche.model';
import { CocheFormService, CocheFormGroup } from './coche-form.service';

@Component({
  standalone: true,
  selector: 'jhi-coche-update',
  templateUrl: './coche-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CocheUpdateComponent implements OnInit {
  isSaving = false;
  coche: ICoche | null = null;

  marcasSharedCollection: IMarca[] = [];
  modelosSharedCollection: IModelo[] = [];

  protected cocheService = inject(CocheService);
  protected cocheFormService = inject(CocheFormService);
  protected marcaService = inject(MarcaService);
  protected modeloService = inject(ModeloService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CocheFormGroup = this.cocheFormService.createCocheFormGroup();

  compareMarca = (o1: IMarca | null, o2: IMarca | null): boolean => this.marcaService.compareMarca(o1, o2);

  compareModelo = (o1: IModelo | null, o2: IModelo | null): boolean => this.modeloService.compareModelo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coche }) => {
      this.coche = coche;
      if (coche) {
        this.updateForm(coche);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coche = this.cocheFormService.getCoche(this.editForm);
    if (coche.id !== null) {
      this.subscribeToSaveResponse(this.cocheService.update(coche));
    } else {
      this.subscribeToSaveResponse(this.cocheService.create(coche));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoche>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(coche: ICoche): void {
    this.coche = coche;
    this.cocheFormService.resetForm(this.editForm, coche);

    this.marcasSharedCollection = this.marcaService.addMarcaToCollectionIfMissing<IMarca>(this.marcasSharedCollection, coche.marca);
    this.modelosSharedCollection = this.modeloService.addModeloToCollectionIfMissing<IModelo>(this.modelosSharedCollection, coche.modelo);
  }

  protected loadRelationshipsOptions(): void {
    this.marcaService
      .query()
      .pipe(map((res: HttpResponse<IMarca[]>) => res.body ?? []))
      .pipe(map((marcas: IMarca[]) => this.marcaService.addMarcaToCollectionIfMissing<IMarca>(marcas, this.coche?.marca)))
      .subscribe((marcas: IMarca[]) => (this.marcasSharedCollection = marcas));

    this.modeloService
      .query()
      .pipe(map((res: HttpResponse<IModelo[]>) => res.body ?? []))
      .pipe(map((modelos: IModelo[]) => this.modeloService.addModeloToCollectionIfMissing<IModelo>(modelos, this.coche?.modelo)))
      .subscribe((modelos: IModelo[]) => (this.modelosSharedCollection = modelos));
  }
}
