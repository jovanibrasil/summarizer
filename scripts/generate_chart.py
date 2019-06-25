import matplotlib
import matplotlib.pyplot as plt
import numpy as np

MARKERS = ['o', 's', 'D']
STYLES = ['-', '--', '-.', ':']
LABELS = ["Exec0", "Exec1", "Exec2", "Exec3", "Exec4"]

def create_plot(series):

    fig, ax = plt.subplots()

    plt.xticks(np.arange(0, 24, 1))
    idx = 0
    for serie in series:
        # Data for plotting
        y = serie   # y
        x = np.arange(0, len(y), 1) # x
        ax.plot(x, y, label=LABELS[idx], linestyle=STYLES[idx], marker=MARKERS[idx], markersize=4)
        idx+=1

    ax.set(xlabel='Geração', ylabel='Aptidão Fuzzy',)
    ax.legend(loc='lower right')
    ax.grid()
    fig.savefig("test.png")
    plt.show()


# Execuções da configuração 18
#  0.4135692498938653
#exec0 = [0.6638077312379194, 0.6638077312379194, 0.6695567954654622, 0.6695567954654622, 0.6695567954654622, 0.671478194846123, 0.671478194846123, 0.671478194846123, 0.672864623913823, 0.6738723990809373, 0.6738723990809373, 0.6740452214091753, 0.6740452214091753, 0.6744941059461209, 0.6744941059461209, 0.6744941059461209, 0.6744941059461209, 0.6744941059461209, 0.7058467266311184, 0.7058467266311184, 0.7058467266311184, 0.7058467266311184, 0.7058467266311184, 0.7058467266311184]
# 0.406197962447473
#exec1 = [0.6595209281135627, 0.6595209281135627, 0.6595209281135627, 0.6595209281135627, 0.6595209281135627, 0.6744731693629804, 0.6744731693629804, 0.6744731693629804, 0.6748590391648537, 0.6749620717749463, 0.6763018361736002, 0.6790698763658709, 0.6790698763658709, 0.6790698763658709, 0.6790698763658709, 0.6790698763658709, 0.6791037169992289, 0.6791037169992289, 0.6791037169992289, 0.6791037169992289, 0.6791037169992289, 0.6791037169992289]
#0.42699887793222454
#exec2 = [0.6617358951802783, 0.6617358951802783, 0.6628589761217629, 0.6628589761217629, 0.6628589761217629, 0.6628589761217629, 0.6649488437584219, 0.6656882675000001, 0.6657772316946309, 0.667743832911972, 0.6683175220908464, 0.6683175220908464, 0.6683175220908464, 0.6683175220908464, 0.7091592692516543, 0.7091592692516543, 0.7091592692516543, 0.7091592692516543, 0.7091592692516543, 0.7091592692516543]

# Execuções da configuração 3

#0.4326143158569401         
exec0 = [0.653366961519982, 0.653366961519982, 0.6546980814596575, 0.6546980814596575, 0.6551628141189269, 0.6804199306641268, 0.6804199306641268, 0.6804199306641268, 0.6804199306641268, 0.6804199306641268, 0.6804199306641268]
#0.4292561622833423         
exec1 = [0.6783882680888291, 0.6783882680888291, 0.6783882680888291, 0.6809812213690045, 0.6809812213690045, 0.6809812213690045, 0.6809812213690045, 0.6809812213690045, 0.6809812213690045]
#0.4325487729387038
exec2 = [0.6669429301339236, 0.6739952873679756, 0.6775821255620014, 0.6775821255620014, 0.6775821255620014, 0.6775821255620014, 0.6775821255620014, 0.6775821255620014, 0.6775821255620014]
create_plot([exec0, exec1, exec2])

