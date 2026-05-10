#include<stdio.h>
#include<stdlib.h>

int main()
{
    int h_us,m_us,s_us;
    printf("Ingrese su Hora Actual: ");
    scanf("%i",&h_us);
    while(h_us>=24 || h_us<=-1)
    {
        printf("Ingrese su Hora Actual: ");
        scanf("%i",&h_us);
    }

    printf("Ingrese sus Minutos Actuales: ");
    scanf("%i",&m_us);
    while(m_us>=60 || m_us<=-1)
    {
        printf("Ingrese sus Minutos Actuales: ");
        scanf("%i",&m_us);
    }

    printf("Ingrese sus Segundos Actuales: ");
    scanf("%i",&s_us);
    while(s_us>=60 || s_us<=-1)
    {
        printf("Ingrese sus Segundos Actuales: ");
        scanf("%i",&s_us);
    }
    while(0==0)
    {
        for(int hor=h_us;hor<24;hor++)
        {
            for(int min=m_us;min<60;min++)
            {
                for(int seg=s_us;seg<60;seg++)
                {
                    printf("%i:%i:%i \n",hor,min,seg);
                }
            s_us=0;
            }
        m_us=0;
        }
    h_us=0;
    }
    return(0);
}
