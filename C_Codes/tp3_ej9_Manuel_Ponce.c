#include<stdio.h>
#include<stdlib.h>

int main()
{
    srand(getpid());
    int num1,num2,cant=0;
    for(int cont=1;cont<101;cont++)
    {
        num1 = rand() % 6;
        num1+=1;
        num2 = rand() % 6;
        num2+=1;
        if(num1+num2==10)
        {
            cant+=1;
        }
    }
    printf("Tras 100 tiradas de dados, hubieron %i dieces \n",cant);
    return(0);
}