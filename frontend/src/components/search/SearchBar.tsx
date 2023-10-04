import React, { useState, useEffect, useCallback, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/search/SearchBar.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'

interface SearchBarProps {
  onSearch: (results: SearchResult[]) => void
}

export interface SearchResult {
  recipeId: number
  title: string
  content: string
  chefId: number
  nickname: string
}

function SearchBar({ onSearch }: SearchBarProps) {
  const navigate = useNavigate()
  const { IcSearch } = useIcon()
  const [text, setText] = useState('')
  const [results, setResults] = useState<SearchResult[]>([])
  const [filteredResults, setFilteredResults] = useState<SearchResult[]>([])
  const $search = useRef<HTMLInputElement | null>(null)
  const $searchedItems = useRef<HTMLUListElement | null>(null)
  const [visible, setVisible] = useState(false)
  const [selectedItemIndex, setSelectedItemIndex] = useState<number | null>(
    null
  )

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    try {
      const fakeResults: SearchResult[] = [
        {
          recipeId: 1,
          title: '참치마요 덮밥',
          content: '레시피 설명 1',
          chefId: 1,
          nickname: '나는 요리사1',
        },
        {
          recipeId: 2,
          title: '훈제오리 냉채',
          content: '레시피 설명 2',
          chefId: 2,
          nickname: '나는 요리사2',
        },
        {
          recipeId: 3,
          title: '마크 정식',
          content: '레시피 설명 3',
          chefId: 3,
          nickname: '나는 요리사3',
        },
        {
          recipeId: 4,
          title: '탕후루',
          content: 'CHEF 4',
          chefId: 4,
          nickname: '나는 요리사4',
        },
      ]

      setResults(fakeResults)
      setFilteredResults(fakeResults)

      onSearch(fakeResults)
    } catch (error) {
      console.error('API 호출 오류:', error)
    }
  }

  const handleSearchChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setText(event.target.value)
      setSelectedItemIndex(null)
    },
    []
  )

  const goLink = useCallback(
    (recipeId: number) => {
      navigate(`/recipe/detail/${recipeId}`)
    },
    [navigate]
  )

  const goSearchList = useCallback(
    (event: React.KeyboardEvent<HTMLInputElement>) => {
      if (event.key === 'ArrowDown') {
        event.preventDefault()
        if (selectedItemIndex === null) {
          setSelectedItemIndex(0)
        } else if (selectedItemIndex < filteredResults.length - 1) {
          setSelectedItemIndex(selectedItemIndex + 1)
        }
      } else if (event.key === 'ArrowUp') {
        event.preventDefault()
        if (selectedItemIndex !== null && selectedItemIndex > 0) {
          setSelectedItemIndex(selectedItemIndex - 1)
        }
      } else if (event.key === 'Enter' && selectedItemIndex !== null) {
        event.preventDefault()
        setVisible(true)
        if (selectedItemIndex !== null && filteredResults.length > 0) {
          goLink(filteredResults[selectedItemIndex].recipeId)
        }
      }
    },
    [filteredResults, goLink, selectedItemIndex]
  )

  const changeTarget = useCallback(
    (event: React.KeyboardEvent<HTMLButtonElement>) => {
      const $target = event.target as HTMLButtonElement
      if (event.key === 'ArrowUp' || event.key === 'ArrowDown') {
        event.preventDefault()

        const $parentLi = $target.parentElement as HTMLLIElement
        const $listItems =
          $searchedItems.current?.querySelectorAll('.js-searchedItem')

        if ($listItems) {
          const currentIndex = Array.from($listItems).indexOf($parentLi)

          if (event.key === 'ArrowUp') {
            if (currentIndex > 0) {
              ;(
                $listItems[currentIndex - 1].querySelector(
                  '.js-searchedItem'
                ) as HTMLButtonElement
              ).focus()
            } else {
              $search?.current?.focus()
            }
          } else if (event.key === 'ArrowDown') {
            if (currentIndex < $listItems.length - 1) {
              ;(
                $listItems[currentIndex + 1].querySelector(
                  '.js-searchedItem'
                ) as HTMLButtonElement
              ).focus()
            }
          }
        }
      }
    },
    [$search, $searchedItems]
  )

  useEffect(() => {
    const filterResults = () => {
      if (text.trim() === '') {
        setVisible(false)
        setFilteredResults([])
      } else {
        const filtered = results.filter(result =>
          result.title.toLowerCase().includes(text.toLowerCase())
        )
        setFilteredResults(filtered)
        setVisible(filtered.length > 0)
      }
    }

    filterResults()
  }, [text, results])

  return (
    <>
      <S.SearchForm onSubmit={handleSubmit}>
        <S.SearchInput
          type="text"
          placeholder="검색"
          value={text}
          onChange={handleSearchChange}
          onKeyDown={goSearchList}
          ref={$search}
        />
        <S.SearchButton type="submit">
          <IcSearch size={3} color={palette.textPrimary} />
        </S.SearchButton>
      </S.SearchForm>
      {visible && (
        <S.DropdownContent ref={$searchedItems}>
          {' '}
          {filteredResults.map((product, index) => (
            <li key={product.recipeId}>
              <S.SearchedItem
                type="button"
                onClick={() => goLink(product.recipeId)}
                onKeyDown={changeTarget}
                className={`js-searchedItem ${
                  selectedItemIndex === index ? 'selected' : ''
                }`}
              >
                <span>{product.title}</span>
              </S.SearchedItem>
            </li>
          ))}
        </S.DropdownContent>
      )}
    </>
  )
}

export default SearchBar
