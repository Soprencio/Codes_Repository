#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include<string.h>

typedef struct BASE 
{
    int dato;
    struct BASE *sig;
} NODO;

void insertarNodo(NODO **, int);

int main(void)
{
    NODO *prim = NULL;
    int dato = 0;
    do
    {
        printf("Ingrese un número (-1 para salir): ");
        scanf("%i",&dato);
        if(dato!=-1)
        {
            insertarNodo(&prim,dato);
        }
    } while(dato != -1);
}

void insertarNodo(NODO **p, int d)
{
    NODO *aux, *nuevo, *aux2;
    int cont = 0;
    aux = *p;
    aux2 = *p;
    nuevo = malloc(sizeof(NODO));
    if(nuevo!=NULL)
    {
        nuevo->dato=d;
        nuevo->sig=NULL;
        if(aux==NULL)
        {
            *p = nuevo;
        }
        else
        {
            while(aux->sig!=NULL && aux->dato<d)
            {
                aux=aux->sig;
                if(cont == 1)
                {
                    aux2=aux2->sig;
                }
                cont = 1;
            }
            if(aux->dato>=d)
            {
                if(cont==1)
                {
                    nuevo->sig = aux2->sig;
                    aux2->sig = nuevo;
                }
                else
                {
                    nuevo->sig = aux2;
                    *p = nuevo;
                }
            }
            else
            {
                aux->sig=nuevo;
            }
        }
        aux = *p;
        while(aux->sig!=NULL)
        {
            printf("%i ",aux->dato);
            aux=aux->sig;
        }
        printf("%i ",aux->dato);
        printf("\n");
    }
}