import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loggedIn = new BehaviorSubject<boolean>(this.isTokenValid());
  loggedIn$ = this.loggedIn.asObservable();

  constructor() {}

  private isTokenValid(): boolean {
    const token = sessionStorage.getItem('jwtToken');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1])); 
      const now = Math.floor(new Date().getTime() / 1000);
      return payload.exp > now;
    }
    return false;
  }

  isAuthenticated(): boolean {
    return this.isTokenValid();
  }

  getUsuario(): string | null {
    const token = sessionStorage.getItem('jwtToken');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub || null; 
    }
    return null;
  }

  getRol(): string | null {
    const token = sessionStorage.getItem('jwtToken');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.rol || null;  
    }
    return null;
  }

  setLoggedIn(status: boolean): void {
    this.loggedIn.next(status);
  }

  logout(): void {
    sessionStorage.removeItem('jwtToken');
    this.setLoggedIn(false);
  }
}