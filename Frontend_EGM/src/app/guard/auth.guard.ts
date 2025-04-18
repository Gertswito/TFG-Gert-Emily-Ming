import { inject, Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  protected authService = inject(AuthService);
  protected router = inject(Router);

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const expectedRole = route.data['expectedRole'];
    const userRole = this.authService.getRol();

    if (this.authService.isAuthenticated()) {
      if (!expectedRole || userRole === expectedRole) {
        return true;
      } else {
        this.router.navigate(['/error-sin-autorizacion']);
        return false;
      }
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
