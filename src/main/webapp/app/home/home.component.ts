import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PruebaComponent } from "../layouts/prueba/prueba.component";
import { FormsModule } from '@angular/forms';
import { EncuestaComponent } from 'app/encuesta/encuesta.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    standalone: true,
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrl: './home.component.scss',
    imports: [SharedModule, RouterModule, PruebaComponent, FormsModule, EncuestaComponent]
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  textoEjemplo?: string;

  private readonly destroy$ = new Subject<void>();

  private modalService = inject(NgbModal);
  private accountService = inject(AccountService);
  private router = inject(Router);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  ngOpenEncuesta(): void {
    this.modalService
     .open(EncuestaComponent, { size: 'lg' })
  }
}
