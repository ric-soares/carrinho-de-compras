import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * Classe que representa o carrinho de compras de um cliente.
 */
public class CarrinhoCompras {

    List<Item> itensCarrinho = new ArrayList<>();

    /**
     * Permite a adição de um novo item no carrinho de compras.
     *
     * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
     * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro.
     * - Se o valor unitário informado for diferente do valor unitário atual do item, o novo valor unitário do item deverá ser
     * o passado como parâmetro.
     *
     * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras.
     *
     * @param produto nãa pode ser nulo.
     * @param valorUnitario não pode ser nulo.
     * @param quantidade deve ser maior que zero.
     * @throws QuantidadeInvalidaException se a quantidade for menor que zero.
     */
    public void adicionarItem(Produto produto, BigDecimal valorUnitario, int quantidade) {
        Objects.requireNonNull(produto, "Produto não pode ser nulo");
        Objects.requireNonNull(valorUnitario, "Valor unitário não pode ser nulo");

        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException("Quantidade inválida: " + quantidade);
        }

        itensCarrinho.stream()
                .filter(item -> item.getProduto().getCodigo().equals(produto.getCodigo()))
                .findFirst()
                .ifPresentOrElse(item -> {
                    item.setQuantidade(item.getQuantidade() + quantidade);
                    if (!Objects.equals(item.getValorUnitario(), valorUnitario)) {
                        item.setValorUnitario(valorUnitario);
                    }
                }, () -> itensCarrinho.add(new Item(produto, valorUnitario, quantidade)));
    }

    /**
     * Permite a remoção do item que representa este produto do carrinho de compras.
     *
     * @param produto o produto a ser removido
     * @return true se o produto foi encontrado e removido, false caso contrário
     * @throws NullPointerException se o produto for nulo
     */
    public boolean removerItem(Produto produto) {
        Objects.requireNonNull(produto, "Produto não pode ser nulo");

        return itensCarrinho.removeIf(
                item -> item.getProduto().getCodigo().equals(produto.getCodigo())
        );
    }

    /**
     * Permite a remoção do item de acordo com a posição.
     * Essa posição deve ser determinada pela ordem de inclusão do produto na
     * coleção, em que zero representa o primeiro item.
     *
     * @param posicaoItem a posição do item na lista
     * @return true se o item foi removido com sucesso
     * @throws IllegalArgumentException se a posição for inválida
     */
    public boolean removerItem(int posicaoItem) {
        if (posicaoItem >= 0 && posicaoItem < itensCarrinho.size()) {
            itensCarrinho.remove(posicaoItem);
            return true;
        } else {
            throw new IllegalArgumentException("Posição do item inválida");
        }
    }

    /**
     * Retorna o valor total do carrinho de compras, que deve ser a soma dos valores totais
     * de todos os itens que compõem o carrinho.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTotal() {
        return itensCarrinho.stream()
                .map(Item::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retorna a lista de itens do carrinho de compras.
     *
     * @return uma cópia da lista de itens
     */
    public Collection<Item> getItens() {
        return new ArrayList<>(itensCarrinho);
    }

    public static class QuantidadeInvalidaException extends RuntimeException {
        public QuantidadeInvalidaException(String mensagem) {
            super(mensagem);
        }
    }
}
