#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<time.h>

int *crearVec(int);
int cargarVec(int *, int);
void buscar_p(int *, int);
void ordenarMenMay(int *, int);

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
    printf("%i \n",tam1);
    ordenarMenMay(vec,tam1);
    buscar_p(vec,tam1);
    return(0);
}

int *crearVec(int i)
{
    int *v = malloc(sizeof(int)*i);
    return(v);
}

void buscar_p(int *v, int i)
{
    int p, aux = 0, posi = 0;
    printf("Ingrese un número que quiera buscar en el vector: ");
    scanf("%i",&p);
    for(int cont = 0; cont<i; cont++)
    {
        if(v[cont] == p)
        {
            aux = 1;
            posi = cont;
        }
    }
    if(aux == 1)
    {
        printf("El número %i se repite en la posición %i \n",p,posi);
    }
    else
    {
        printf("El número %i no se repite en el vector \n",p);
    }
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