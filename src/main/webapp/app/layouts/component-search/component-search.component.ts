import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ICoche } from '../../entities/coche/coche.model';

@Component({
  selector: 'jhi-component-search',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './component-search.component.html',
  styleUrl: './component-search.component.scss'
})
export class ComponentSearchComponent {
  @Output() sendSearch = new EventEmitter<string>();

  textSearch: string = '';

  enviarSearch () {
    this.sendSearch.emit(this.textSearch.toLowerCase());
  }

  static validarSearch(data: ICoche[], textSearch: String): ICoche | void {
    let cochesFitrados: ICoche[] = [];
    if(textSearch != ''){
      for (const coche of data){
        for (const campo of Object.values(coche)) {
          if(campo != null){
            if(typeof campo !== 'object'){
              if(campo.toString().toLowerCase().includes(textSearch)){
                
              }
            }
          }
        }
      }
    }else{
      return data;
    }
  }
}
