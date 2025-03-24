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
import { ClienteAjustesComponent } from './entities/cliente/cliente-ajustes/cliente-ajustes.component';
import { ClienteCuentaComponent } from './entities/cliente/cliente-cuenta/cliente-cuenta.component';
import { CarritoComponent } from './layouts/carrito/carrito.component';
import { ProductoListComponent } from './entities/producto/producto-list/producto-list.component';
import { SubcategoriaListComponent } from './entities/subcategoria/subcategoria-list/subcategoria-list.component';
import { ContrasenhaComponent } from './layouts/contraseña/contrasenha.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'categoria', component: CategoriaComponent },
    { path: 'subcategoria', component: SubcategoriaComponent },
    { path: 'subcategoria-list', component: SubcategoriaListComponent },
    { path: 'cliente', component: ClienteComponent },
    { path: 'cliente-ajustes', component: ClienteAjustesComponent },
    { path: 'cliente-cuenta', component: ClienteCuentaComponent },
    { path: 'direccion', component: DireccionComponent },
    { path: 'pago', component: PagoComponent },
    { path: 'producto', component: ProductoComponent },
    { path: 'producto-list', component: ProductoListComponent },
    { path: 'lineasVenta', component: LineasVentaComponent },
    { path: 'venta', component: VentaComponent},
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'carrito', component: CarritoComponent },
    { path: 'admin-home', component: AdminHomeComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'recuperar-contrasenha', component: ContrasenhaComponent }, // Ruta para recuperar contraseña
    { path: 'error-sin-autorizacion', component: ErrorComponent },
];
