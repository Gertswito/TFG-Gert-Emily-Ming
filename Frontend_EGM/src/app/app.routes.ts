import { Routes } from '@angular/router';
import { CategoriaComponent } from './entities/categoria/categoria.component';
import { SubcategoriaComponent } from './entities/subcategoria/subcategoria.component';
import { ClienteComponent } from './entities/cliente/cliente.component';
import { DireccionComponent } from './entities/direccion/direccion.component';
import { PagoComponent } from './entities/pago/pago.component';
import { ProductoComponent } from './entities/producto/producto.component';
import { LineasVentaComponent } from './entities/lineasVenta/lineasVenta.component';
import { VentaComponent } from './entities/venta/venta.component';
import { HomeComponent } from './layouts/home/home.component';
import { LoginComponent } from './layouts/login/login.component';
import { RegistroComponent } from './layouts/registro/registro.component';
import { AdminHomeComponent } from './layouts/admin-home/admin-home.component';
import { AuthGuard } from './guard/auth.guard';
import { ErrorComponent } from './error/error.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'categoria', component: CategoriaComponent },
    { path: 'subcategoria', component: SubcategoriaComponent },
    { path: 'cliente', component: ClienteComponent },
    { path: 'direccion', component: DireccionComponent },
    { path: 'pago', component: PagoComponent },
    { path: 'producto', component: ProductoComponent },
    { path: 'lineasVenta', component: LineasVentaComponent },
    { path: 'venta', component: VentaComponent},
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'admin-home', component: AdminHomeComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'error-sin-autorizacion', component: ErrorComponent },
];
