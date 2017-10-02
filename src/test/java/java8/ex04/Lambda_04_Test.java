package java8.ex04;


import java8.data.Account;
import java8.data.Data;
import java8.data.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Exercice 04 - FuncCollection
 * Exercice synthèse des exercices précédents
 */
public class Lambda_04_Test {

	// tag::interfaces[]
	interface GenericPredicate<T> {
		boolean test(T o);
	}

	interface GenericMapper<E, S> {
		S mapi(E el);
	}

	interface Processor<T> {
		void process(T o);
	}
	// end::interfaces[]

	// tag::FuncCollection[]
	class FuncCollection<T> {

		private Collection<T> list = new ArrayList<>();

		public void add(T a) {
			list.add(a);
		}

		public void addAll(Collection<T> all) {
			for(T el:all) {
				list.add(el);
			}
		}
		// end::FuncCollection[]

		// tag::methods[]
		private FuncCollection<T> filter(GenericPredicate<T> predicate) {
			FuncCollection<T> result = new FuncCollection<>();
			for(T el:this.list){
				if(predicate.test(el)){
					result.add(el);
				}
			}
			return result;
		}

		private <S> FuncCollection<S> map(GenericMapper<T, S> mapper) {
			FuncCollection<S> result = new FuncCollection<>();
			for(T el:this.list){
				result.add(mapper.mapi(el));
			}
			return result;
		}

		private void forEach(Processor<T> processor) {
			for(T o:this.list){
				processor.process(o);
			}
		}
		// end::methods[]

	}



	// tag::test_filter_map_forEach[]
	@Test
	public void test_filter_map_forEach() throws Exception {

		List<Person> personList = Data.buildPersonList(100);
		FuncCollection<Person> personFuncCollection = new FuncCollection<>();
		personFuncCollection.addAll(personList);

		personFuncCollection
		// TODO filtrer, ne garder uniquement que les personnes ayant un age > 50
		.filter(o -> o.getAge() > 50)
		// TODO transformer la liste de personnes en liste de comptes. Un compte a par défaut un solde à 1000.
		.map(p->{
			Account a = new Account();
			a.setBalance(1000);
			a.setOwner(p);
			return a;
		})
		// TODO vérifier que chaque compte a un solde à 1000.
		// TODO vérifier que chaque titulaire de compte a un age > 50
		.forEach(o-> {
			assert 	o.getBalance() == 1000;
			assert o.getOwner().getAge() > 50;
		});

	}
	// end::test_filter_map_forEach[]

	// tag::test_filter_map_forEach_with_vars[]
	@Test
	public void test_filter_map_forEach_with_vars() throws Exception {

		List<Person> personList = Data.buildPersonList(100);
		FuncCollection<Person> personFuncCollection = new FuncCollection<>();
		personFuncCollection.addAll(personList);

		// TODO créer un variable filterByAge de type GenericPredicate
		// TODO filtrer, ne garder uniquement que les personnes ayant un age > 50
		GenericPredicate<Person> filterByAge = p -> p.getAge()>50;

		// TODO créer un variable mapToAccount de type GenericMapper
		// TODO transformer la liste de personnes en liste de comptes. Un compte a par défaut un solde à 1000.
		GenericMapper<Person, Account> mapToAccount = p -> {
			Account a = new Account();
			a.setOwner(p);
			a.setBalance(1000);
			return a;
		};

		// TODO créer un variable verifyAccount de type GenericMapper
		// TODO vérifier que chaque compte a un solde à 1000.
		// TODO vérifier que chaque titulaire de compte a un age > 50
		Processor<Account> verifyAccount = a -> {
			assert a.getBalance() == 1000;
			assert a.getOwner().getAge() > 50;
		};

		
        personFuncCollection
                .filter(filterByAge)
                .map(mapToAccount)
                .forEach(verifyAccount);
		 
	}
	// end::test_filter_map_forEach_with_vars[]


}
