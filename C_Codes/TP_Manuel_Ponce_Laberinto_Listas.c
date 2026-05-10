#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<time.h>

typedef struct BASE {
    int x;
    int y;
    struct BASE *sig;
}NODO;

int **crearMat(int, int);
void cargaMat(int **, int, int);
void mostrarMat(int **, int, int);
void busca_ceros(int **, int, int, char*, int*,NODO**p);
void posxy(int, int, char*);
char* creaVec(int);
char* reset();
void espera();
int main(void)
{
    srand(time(NULL));
    system("clear");
    int **m;
    char *pos;
    m = crearMat(6,6);
    cargaMat(m,6,6);
    mostrarMat(m,6,6);
    pos = creaVec(15);
    int* cont_gana;
    cont_gana = malloc(sizeof(int)*1);
    cont_gana[0] = 1;
    posxy(8,1,pos);
    printf("%s",pos);
    printf("\033[1;34mCaminos Encontrados:");
    NODO *prim = NULL;
    busca_ceros(m,0,0,pos,cont_gana,&prim);
}

//  "\033[1;31m <texto en rojo> \033[0;m <texto en blanco>"

int ** crearMat(int f, int c)
{
    int **m = (int**) malloc(sizeof(int**)*f);
    for(int cont = 0; cont<f; cont++)
    {
        m[cont] = (int*) malloc(sizeof(int*)*c);
    }
    return(m);
}

char *creaVec(int f)
{
    char *v = malloc(sizeof(char*)*f);
    return(v);
}

void posxy(int x, int y, char*v)
{
    char fi[3]={'\0','\0','\0'}, co[3]={'\0','\0','\0'};
    char cadpos[15] = {'\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0','\0'};
    sprintf(fi,"%d",x);
    sprintf(co,"%d",y);
    strcat(cadpos,"\033[");
    strcat(cadpos,fi);
    strcat(cadpos,";");
    strcat(cadpos,co);
    strcat(cadpos,"H");
    strcpy(v,cadpos);
}

void espera(double seg)
{
    if(seg<0)return;
    #ifdef _WIN32
        seg *= 1000;
        int goal = (int) seg + clock();
        while(goal>clock());
    #else
        seg *= 1000000;
        int goal = (int) seg;
        usleep(goal);
    #endif
}

char* reset()
{
    return("\033[0;m");
}

void cargaMat(int **m, int f, int c)
{
    //seccion en verde para un patron random - quitar m[fil][col] = 1 y los m[][] = 0
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            //m[fil][col] = rand() % 2;
            m[fil][col] = 1;
        }
    }
    //m[0][0] = 0;
    m[0][0] = 0;
    m[0][1] = 0;
    m[1][2] = 0;
    m[1][4] = 0;
    m[1][5] = 0;
    m[2][1] = 0;
    m[2][3] = 0;
    m[3][2] = 0;
    m[3][4] = 0;
    m[4][2] = 0;
    m[5][1] = 0;
    m[4][5] = 0;
    m[5][3] = 0;
    m[5][5] = 0;
}

void mostrarMat(int **m, int f, int c)
{
    char *vec_aux;
    vec_aux = creaVec(15);
    for(int fil = 0; fil<f; fil++)
    {
        for(int col = 0; col<c; col++)
        {
            posxy(fil+1,(col+1)*2,vec_aux);
            printf("%s%i",vec_aux,m[fil][col]);
        }
    }
    espera(0.3);
}

void busca_ceros(int **m, int x, int y, char* pos, int* cont,NODO**p)
{
    if(x!=6)
    {
        posxy(x+1,(y+1)*2,pos);
        printf("%s%s%i\n",pos,"\033[1;31m",m[x][y]);
        m[x][y] = 3;
        espera(0.3);
        NODO *aux, *nuevo;
        aux = *p;
        nuevo = malloc(sizeof(NODO));
        if(nuevo!=NULL)
        {
            nuevo->x=x;
            nuevo->y=y;
            nuevo->sig=NULL;
            if(aux==NULL)
            {
                *p = nuevo;
            }
            else
            {
                while(aux->sig!=NULL)
                {
                    aux=aux->sig;
                }
                aux->sig=nuevo;
            }
        }
        if(x==5)
        {
            posxy(8,22,pos);
            printf("%s%i",pos,cont[0]);
            aux = *p;
            int cont2 = 0;
            posxy(8+cont[0],1,pos);
            printf("%sCam%i: ",pos,cont[0]);
            while(aux->sig!=NULL)
            {
                posxy(8+cont[0],7+cont2,pos);
                printf("%s(%i:%i)",pos,aux->x,aux->y);
                cont2+=6;
                aux=aux->sig;
            }
            posxy(8+cont[0],7+cont2,pos);
            printf("%s(%i:%i)",pos,aux->x,aux->y);
            cont[0]++;
        }
        else
        {
            if(x>0 && m[x-1][y]==0)
            {
                busca_ceros(m,x-1,y,pos,cont,p);
            }
            if(x>0 && y<5 && m[x-1][y+1]==0)
            {
                busca_ceros(m,x-1,y+1,pos,cont,p);
            }
            if(y<5 && m[x][y+1]==0)
            {
                busca_ceros(m,x,y+1,pos,cont,p);
            }
            if(x<5 && y<5 && m[x+1][y+1]==0)
            {
                busca_ceros(m,x+1,y+1,pos,cont,p);
            }
            if(x<5 && m[x+1][y]==0)
            {
                busca_ceros(m,x+1,y,pos,cont,p);
            }
            if(x<5 && y>0 && m[x+1][y-1]==0)
            {
                busca_ceros(m,x+1,y-1,pos,cont,p);
            }
            if(y>0 && m[x][y-1]==0)
            {
                busca_ceros(m,x,y-1,pos,cont,p);
            }
            if(y>0 && x>0 && m[x-1][y-1]==0)
            {
                busca_ceros(m,x-1,y-1,pos,cont,p);
            }
        }
        m[x][y] = 0;
        posxy(x+1,(y+1)*2,pos);
        printf("%s%s%i\n",pos,reset(),m[x][y]);
        espera(0.3);
        NODO *aux2;
        aux = *p;
        aux2 = *p;
        int conta = 0;
        while(aux->sig!=NULL)
        {
            aux=aux->sig;
            if(conta == 1)
            {
                aux2=aux2->sig;
            }
            conta = 1;
        }
        aux2->sig=NULL;
    }
}