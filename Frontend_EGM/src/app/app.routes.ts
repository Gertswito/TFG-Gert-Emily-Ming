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
import { CarritoComponent } from './layouts/carrito/carrito.component';
import { ProductoListComponent } from './entities/producto/producto-list/producto-list.component';
import { SubcategoriaListComponent } from './entities/subcategoria/subcategoria-list/subcategoria-list.component';
import { CategoriaCreateComponent } from './entities/categoria/categoria-create/categoria-create.component';
import { SubcategoriaCreateComponent } from './entities/subcategoria/subcategoria-create/subcategoria-create.component';
import { ClienteCreateComponent } from './entities/cliente/cliente-create/cliente-create.component';
import { ProductoCreateComponent } from './entities/producto/producto-create/producto-create.component';
import { LineasVentaCreateComponent } from './entities/lineasVenta/lineasVenta-create/lineasVenta-create.component';
import { VentaCreateComponent } from './entities/venta/venta-create/venta-create.component';
import { DireccionCreateComponent } from './entities/direccion/direccion-create/direccion-create.component';
import { PagoCreateComponent } from './entities/pago/pago-create/pago-create.component';
import { ProductoInfoComponent } from './entities/producto/producto-info/producto-info.component';
import { CompraComponent } from './layouts/compra/compra.component';
import { CompraExitoComponent } from './layouts/compra/compra-exito/compra-exito.component';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: HomeComponent },
    { path: 'categoria', component: CategoriaComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'categoria-create', component: CategoriaCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'subcategoria', component: SubcategoriaComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'subcategoria-list', component: SubcategoriaListComponent },
    { path: 'subcategoria-create', component: SubcategoriaCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'cliente', component: ClienteComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'cliente-ajustes', component: ClienteAjustesComponent },
    { path: 'cliente-create', component: ClienteCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'direccion', component: DireccionComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'direccion-create', component: DireccionCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'pago', component: PagoComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' }  },
    { path: 'pago-create', component: PagoCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'producto', component: ProductoComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' }  },
    { path: 'producto-list', component: ProductoListComponent },
    { path: 'producto-create', component: ProductoCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'producto-info', component: ProductoInfoComponent },
    { path: 'lineasVenta', component: LineasVentaComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'lineasVenta-create', component: LineasVentaCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'venta', component: VentaComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'venta-create', component: VentaCreateComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'carrito', component: CarritoComponent, canActivate: [AuthGuard] },
    { path: 'compra', component: CompraComponent },
    { path: 'admin-home', component: AdminHomeComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
    { path: 'error-sin-autorizacion', component: ErrorComponent },
    { path: 'compra-exito', component: CompraExitoComponent },
];
