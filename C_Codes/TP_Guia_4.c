#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<string.h>

int *crearVec(int);
void cargaVec(int *, int);
void mostrar(int *, int);
void kesima(int *, int);

int main(void)
{
    srand(time(NULL));
    int *vec;
    int num;
    printf("Ingrese el largo del vector: ");
    scanf("%i",&num);
    vec = crearVec(num);
    cargaVec(vec,num);
    mostrar(vec,num);
    kesima(vec,num);
}

int *crearVec(int i)
{
    int *v = malloc(sizeof(int)*i);
    return(v);
}

void cargaVec(int *v, int i)
{
    for(int cont = 0; cont<i; cont++)
    {
        v[cont] = rand() % 100;
    }
}

void mostrar(int *v, int i)
{
    for(int cont = 0; cont<i; cont++)
    {
        printf("%i-",v[cont]);
    }
    printf("\n");
}

void kesima(int *v, int i)
{
    int k;
    printf("Ingrese un numero para buscar un caracter en esa posición: ");
    scanf("%i",&k);
    while(k<0 || k>=i)
    {
        printf("Error, no puedes buscar un dato en una posición inexistente \n");
        printf("Ingrese un numero para buscar un caracter en esa posición: ");
        scanf("%i",&k);
    }
    printf("%i \n",v[k]);
}