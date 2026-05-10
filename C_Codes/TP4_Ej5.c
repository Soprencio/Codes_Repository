#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int **crearMat(int,int);
void cargaMat(int **,int,int);
void mostrarMat(int**,int,int);

void vecPrin(int**);
void vecSec(int**);
void sumaFil(int**);
void sumaCol(int**);
void sumaTotal(int**);

int main(void)
{
    int **mat;
    int aux = 1, num;
    mat = crearMat(3,3);
    cargaMat(mat,3,3);
    mostrarMat(mat,3,3);
    while(aux==1)
    {
        printf("Ingrese un número en base a estas opciones: \n 1) Cargar Diagonal Principal y mostrar \n 2) Cargar Diagonal Secundaria y mostrar \n 3) Mostrar suma de cada fila \n 4) Mostrar suma de cada columna \n 5) Mostrar suma total \n 0) Salir \n Elegir: ");
        scanf("%i",&num);
        while(num<0 || num>5)
        {
            printf("Ingrese un número en base a estas opciones: \n 1) Cargar Diagonal Principal y mostrar \n 2) Cargar Diagonal Secundaria y mostrar \n 3) Mostrar suma de cada fila \n 4) Mostrar suma de cada columna \n 5) Mostrar suma total \n 0) Salir \n Elegir: ");
            scanf("%i",&num);
        }
        switch(num)
        {
            case 1:
            {
                vecPrin(mat);
                break;
            }
            case 2:
            {
                vecSec(mat);
                break;
            }
            case 3:
            {
                sumaFil(mat);
                break;
            }
            case 4:
            {
                sumaCol(mat);
                break;
            }
            case 5:
            {
                sumaTotal(mat);
                break;
            }
            case 0:
            {
                aux = 0;
                break;
            }
        }
    }
}

void sumaTotal(int **m)
{
    int aux = 0;
    for(int fil = 0; fil<3; fil++)
    {
        for(int col = 0; col<3; col++)
        {
            aux+=m[fil][col];
        }
    }
    printf("La suma total de la matriz es %i \n",aux);
}

void sumaCol(int **m)
{
    int aux = 0;
    for(int col = 0; col<3; col++)
    {
        for(int fil = 0; fil<3; fil++)
        {
            aux+=m[fil][col];
        }
        printf("La suma de la columna %i es %i \n",col,aux);
        aux = 0;
    }
}

void sumaFil(int **m)
{
    int aux = 0;
    for(int fil = 0; fil<3; fil++)
    {
        for(int col = 0; col<3; col++)
        {
            aux+=m[fil][col];
        }
        printf("La suma de la fila %i es %i \n",fil,aux);
        aux = 0;
    }
}

void vecSec(int **m)
{
    int v[3];
    int cont2 = 2;
    for(int cont = 0; cont<3; cont++)
    {
        v[cont] = m[cont][cont2];
        cont2--;
    }
    printf("Diagonal Secundaria: \n");
    for(int vec = 0; vec<3; vec++)
    {
       printf("%i ",v[vec]);
    }
    printf("\n");
}

void vecPrin(int **m)
{
    int v[3];
    for(int cont = 0; cont<3; cont++)
    {
        v[cont] = m[cont][cont];
    }
    printf("Diagonal Principal: \n");
    for(int vec = 0; vec<3; vec++)
    {
       printf("%i ",v[vec]);
    }
    printf("\n");
}

int **crearMat(int fil, int col)
{
    int **m = (int **)malloc(sizeof(int **)*fil);
    for(int cont = 0; cont<fil; cont++)
    {
        m[cont] = (int *)malloc(sizeof(int *)*col);
    }
    return(m);
}

void mostrarMat(int **mat, int f, int c)
{
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            printf("%i ",mat[fil][col]);
        }
        printf("\n");
    }
    printf("\n");
}

void cargaMat(int **m, int f, int c)
{
    int num;
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            printf("Ingrese el dato %i %i de la matriz: ",fil,col);
            scanf("%i",&num);
            m[fil][col] = num;
        }
    }
}
