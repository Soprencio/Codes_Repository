#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<time.h>

int *crearVec(int);
int cargarVec(int *, int);
void ordenarMenMay(int *, int);
void intercalar(int *, int);
void mostrar(int *, int);

int main(void)
{
    int * vec;
    int ext;
    printf("Ingrese el largo de su vector (1-20): ");
    scanf("%i",&ext);
    while(ext<1 || ext>20)
    {
        printf("Ingrese el largo de su vector (1-20): ");
        scanf("%i",&ext);
    }
    vec = crearVec(ext);
    int tam1 = cargarVec(vec,ext);
    ordenarMenMay(vec,tam1);
    mostrar(vec,tam1);
    intercalar(vec,tam1);
    int tam2 = tam1 + 1;
    mostrar(vec,tam2);
    ordenarMenMay(vec,tam2);
    mostrar(vec,tam2);
    return(0);
}

int *crearVec(int i)
{
    int e = i+1;
    int *v = malloc(sizeof(int)*e);
    return(v);
}

int cargarVec(int *v, int i)
{
    int aux = 0, num, cont = 0;
    while(aux==0)
    {
        if(cont<i)
        {
            printf("Ingrese un dato del vector (1-100) (Ingrese 0 para dejar de ingresar): ");
            scanf("%i",&num);
        }
        else
        {
            num = 0;
        }
        while(num>100 || num<0)
        {
            printf("Ingrese un dato entre 1-100 (Ingrese 0 para dejar de ingresar): ");
            scanf("%i",&num);
        }
        if(num>0 && cont<101)
        {
            v[cont] = num;
        }
        else
        {
            aux = 1;
            cont--;
        }
        cont++;
    }
    return(cont);
}

void ordenarMenMay(int *v, int i)
{
    int aux = 0;
    for(int cont = 0; cont<i-1; cont++)
    {
        for(int cont2 = cont+1; cont2<i; cont2++)
        {
            if(v[cont]>v[cont2])
            {
                aux = v[cont];
                v[cont] = v[cont2];
                v[cont2] = aux;
            }
        }
    }
}

void intercalar(int *v, int i)
{
    int p;
    printf("Ingrese un nuevo número para el vector: ");
    scanf("%i",&p);
    v[i] = p;
}

void mostrar(int *v, int i)
{
    for(int cont = 0; cont<i; cont++)
    {
        if(i-cont != 1)
        {
            printf("%i-",v[cont]);
        }
        else
        {
            printf("%i \n", v[cont]);
        }
    }
}